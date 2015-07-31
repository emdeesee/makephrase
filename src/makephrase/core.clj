(ns makephrase.core
  (:require [clojure.string :as str])
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def ^:dynamic *words*)

(defn parse-words-file [wordsfile]
  (->> (slurp wordsfile)
       str/split-lines
       (map str/trim)
       (filter #(re-matches #"^[a-z]{3,8}$" %))))

(defn apply-to-random-element [f coll]
  "return coll with a random element x replaced by (f x)"
  (if (empty? coll)
    coll
    (let [n (rand-int (count coll))
          e (f (nth coll n))]
      (concat (take n coll) [e] (drop (inc n) coll)))))

(defn index-nth-if [coll pred n]
  "return the absolute index of the n-th element for which pred is true; nil if not found"
  (->> coll
       (map-indexed #(vector %1 %2))
       (filter #(pred (second %)))
       (#(nth % n nil))
       first))

(defn index-nth
  "return the absolute index of the n-th occurrence; nil if not found"
  [coll x n]
  (index-nth-if coll #{x} n))

(defn nth-random-occurrence
  "get the index of a random occurrence of x in coll; nil if not present"
  [coll x]
  (when-let [freq (get (frequencies coll) x)]
    (index-nth coll x (rand-int freq))))

(defn add-cap [w]
  "capitalize a random element of w"
  (apply str (apply-to-random-element str/upper-case w)))

(defn studlify [w]
  "capitalize every other element of w"
  (let [fns (cycle [identity str/upper-case])]
    (apply str (map #(%1 %2) fns w))))

(defn leetify [w]
  "apply a \"leet\" transformation to the word w"
  (let [leetmap {\a \4 \e \3 \i \! \l \1 \o \0 \s \5 \t \7 \x "XXX"}
        p (re-pattern (format "[%s]" (apply str (keys leetmap))))
        matches (re-seq p w)]
    (if matches
      (let [selection (first (rand-nth matches))
            freqs (frequencies matches)
            which (rand-int (get freqs (str selection)))
            index (index-nth w selection which)]
        (str (subs w 0 index) (get leetmap selection) (subs w (inc index))))
      (studlify w))))

(defn make-phrase
  "produce a string of words suitable for use as a passphrase"
  ([len]
     (->> (for [_ (range len)] (rand-nth *words*))
          (apply-to-random-element add-cap)
          (apply-to-random-element leetify)
          (str/join " ")))
  ([] (make-phrase 4)))

(def options
  "Command line options"
  [["-n" "--how-many <number of phrases>"
    :default 1
    :parse-fn #(Integer/parseInt %)]

   ["-l" "--length <number of words>"
    :default 4
    :parse-fn #(Integer/parseInt %)
    ]

   ["-w" "--words /path/to/words/file"
    :default "/usr/share/dict/words"]

   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Generate relatively strong passphrases for use in credentials"
        "see https://xkcd.com/936/"
        ""
        "Usage: makephrase [options]"
        ""
        "Options:"
        options-summary
        ""]
       (str/join \newline)))

(defn exit [status message]
  (println message)
  (System/exit status))

(defn error-msg [errors]
  (str "Errors:\n\n"
       (str/join \newline errors)))

(defn -main
  "Generate passphrases based on command line flags and input."
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args options)]
    (cond
     (:help options) (exit 0 (usage summary))
     (not (empty? arguments)) (exit 1 (usage summary))
     errors (exit 1 (error-msg errors))
     :else (binding [*words* (parse-words-file (:words options))]
             (doseq [_ (range (:how-many options))]
               (println (make-phrase (:length options))))))))
