(ns tempid-test.service
  (:require [io.pedestal.http :as bootstrap]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [ring.util.response :as ring-resp]
            [om.next.server :as om]))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))
(defonce app-state (atom {}))

(defn readf
  [{:keys [state]} k params]
  )

(defn mutatef
  [{:keys [state]} k params]
  (cond
    (=  k 'person/create)
    {:value {:tempids {[:person/by-id -3] [:person/by-id 3]}}
     :action (fn []
               )}))

(defn api [req]
  (let [payload (:transit-params req)]
    {:status 200
     :body ((om/parser {:read readf
                        :mutate mutatef}) {:state app-state}
             payload)}))

(defn home-page
  [request]
  (->
    (ring-resp/response
      (str "<html><head><title>Test</title></head>"
           "<body>"
           "<div id= \"app\"></div>"
           "<script type= \" text/javascript \" src= \"/js/main.js\"></script>"
           "</body>"
           "</html>"))
      (ring-resp/content-type "text/html")))

(defroutes routes
  ;; Defines "/" and "/about" routes with their associated :get handlers.
  ;; The interceptors defined after the verb map (e.g., {:get home-page}
  ;; apply to / and its children (/about).
  [[["/" {:get home-page}
     ^:interceptors [(body-params/body-params) bootstrap/html-body]
     ["/api" ^:interceptors [bootstrap/transit-json-body] {:post api}]
     ["/about" {:get about-page}]]]])

;; Consumed by tempid-test.server/create-server
;; See bootstrap/default-interceptors for additional options you can configure
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::bootstrap/interceptors []
              ::bootstrap/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::bootstrap/allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              ::bootstrap/resource-path "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ::bootstrap/type :jetty
              ;;::bootstrap/host "localhost"
              ::bootstrap/port 8080})

