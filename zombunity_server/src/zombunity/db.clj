(ns zombunity.db
  (:refer-clojure :exclude [resultset-seq])
  (:require  [clojure.java.jdbc :as jdbc]
             [clojure.data.json :as json :refer [json-str]]
             [zombunity.data :as data]))

(def db {:classname "org.hsqldb.jdbcDriver" ; must be in classpath
         :subprotocol "hsqldb"
         :subname "hsql://localhost/zombunity"
         ; Any additional keys are passed to the driver
         ; as driver-specific properties.
         :user "sa"
         :password ""})

; rs will be a sequence of maps,
; one for each record in the result set.
;"Remove queued server JSON messages and return them"
(defmethod zombunity.data/get-messages ::hsqldb
  []
  (jdbc/with-connection db
    (jdbc/transaction
      (jdbc/with-query-results rs ["select id, json from msg_to_server"]
        (let [rs (doall rs)
              ids (str (apply str "id in (" (interpose "," (map :id rs))) ")")]
          (if (seq rs)
            (jdbc/delete-rows "msg_to_server" [ids]))
          (println rs)
          (->> (map :json rs) (map json/read-json) doall))))))

(defn insert-json
  [tbl m]
  (jdbc/with-connection db
    (jdbc/insert-records tbl {:json (json/json-str m)})))

;"Put a JSON message in the client queue"
(defmethod zombunity.data/msg-client ::hsqldb
  [m]
  (println "sending message to client: " m)
  (insert-json "msg_to_client" m))

;"Put a JSON message in the client queue"
(defmethod zombunity.data/msg-server ::hsqldb
  [m]
  (println "sending message to server: " m)
  (insert-json "msg_to_server" m))


;"Run a select statment and return the results as a list of maps"
(defmethod zombunity.data/select ::hsqldb
  [stmt-params]
  (jdbc/with-connection db
      (jdbc/with-query-results rs stmt-params (doall rs))))

;"Run insert statements for each vector of a table name and a map of column names (keywords or strings) to values"
(defmethod zombunity.data/insert ::hsqldb
  [table-column-vals]
    (jdbc/with-connection db
      (doall (map (fn [[t vs]] (jdbc/insert-record t vs)) table-column-vals))))

;"Run an update statement"
(defmethod zombunity.data/update ::hsqldb
  [table where-params column-values]
  (jdbc/with-connection db
    (jdbc/update-values table where-params column-values)))

;"Return true is this connection is in the middle of logging in"
(defmethod zombunity.data/is-logging-in? ::hsqldb
  [conn-id]
  (seq (zombunity.data/select ["select conn_id from login_state where conn_id = ?" conn-id])))

(defmethod zombunity.data/delete ::hsqldb
  [table where-clauses]
  (jdbc/with-connection db
    (jdbc/delete-rows table where-clauses)))


