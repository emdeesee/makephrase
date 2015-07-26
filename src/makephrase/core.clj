(ns makephrase.core
  (:require [clojure.string :as str])
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def ^:dynamic *words*
  (let [wordsfile "/usr/share/dict/words"]
    (->> (slurp wordsfile)
         str/split-lines
         (map str/trim)
         (filter #(re-matches #"^[a-z]{3,8}$" %)))))

(defn apply-to-random-element [f coll]
  "return coll with a random element x replaced by (f x)"
  (let [n (rand-int (count coll))
        e (f (nth coll n))]
    (concat (take n coll) [e] (drop (inc n) coll))))

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
  (index-nth-if coll #(= x %) n))

(defn add-cap [w]
  (let [n (rand-int (count w))]
    (str (subs w 0 n) (str/capitalize (subs w n)))))

(defn leetify [w]
  (let [leetmap {\a \4 \e \3 \i \! \l \1 \o \0 \s \5 \t \7 \x "XXX"}
        p (re-pattern (format "[%s]" (apply str (keys leetmap))))
        matches (re-seq p w)]
    (if matches
      (let [selection (first (rand-nth matches))
            freqs (frequencies matches)
            which (rand-int (get freqs (str selection)))
            index (index-nth w selection which)]
        (str (subs w 0 index) (get leetmap selection) (subs w (inc index))))
      w)))

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
  [["-n" "--how-many <number to generate>"
    :default 1
    :parse-fn #(Integer/parseInt %)]

   ["-l" "--length <number of words>"
    :default 4
    :parse-fn #(Integer/parseInt %)
    ]

   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Generate a relatively strong pass phrase for use in credentials"
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
     :else (doseq [_ (range (:how-many options))]
             (println (make-phrase (:length options)))))))