(defproject zombunity-mud "0.1.1-SNAPSHOT"
  :description "Web-based mud server with websockets"
  :url "https://github.com/cwmaguire/zombunity-mud"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main zombunity.http
  :repositories {"project" "file:repo"}
  :injections [(require '[redl core complete])]
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [org.clojure/data.json "0.1.3"]
                 [org.clojure/java.classpath "0.2.0"]
                 [org.clojure/tools.namespace "0.2.4"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [org.hsqldb/hsqldb "2.2.8"]
                 [org.hsqldb/sqltool "2.2.8"]
                 [ring "1.2.0"]
                 [redl "0.1.0"]
                 [local/clj "1.0.0"]
                 [local/cljs "1.0.0"]
                 [local/goog "1.0.0"]
                 [local/compiler "1.0.0"]]
  :source-paths ["zombunity_server/src" "zombunity_web/src" "zombunity_http/src"]
  :test-paths ["zombunity_server/test"])
