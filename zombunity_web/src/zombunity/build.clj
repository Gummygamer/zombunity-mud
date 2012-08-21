(ns zombunity.build
  (:require [cljs.closure :as cljsc]
            [clojure.string :as s]))

(defn get-proj-path
  []
  (s/replace (. (java.io.File. ".") getCanonicalPath) #"\\" "/"))

(defn build []
  (let [proj-path (get-proj-path)
        web-path (str proj-path "/zombunity_web/src/")
        http-path (str proj-path "/zombunity_http/resource/")
        js-file (str http-path "zombunity.js")]
    (print proj-path)
    (cljsc/build
    web-path
    {;:optimizations :advanced
     ;:optimizations :simple
     :output-dir http-path
     :output-to js-file})))
