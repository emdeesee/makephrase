(defproject makephrase "0.1.0-SNAPSHOT"
  :description "A passphrase generator"
  :url "http://github.com/emdeesee/makephrase"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.cli "0.3.1"]]
  :plugins [[lein-bin "0.3.5"]]
  :main ^:skip-aot makephrase.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :bin { :name "makephrase" :bootclasspath true})
