(defproject elev "0.1.0-SNAPSHOT"
  :description "A toolkit for simulating elevator control systems."
  :url "https://github.com/jwhitlark/elev"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [table "0.5.0"]]
  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [org.clojure/java.classpath "0.2.2"]
                                  [com.gfredericks/test.chuck "0.1.19"]
                                  [org.clojure/tools.trace "0.7.8"]
                                  [org.clojure/test.check "0.8.0-ALPHA"]]}}
  :main elev.core)
