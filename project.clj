(defproject tempid-test "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0" :scope "provided"]
                 [org.clojure/clojurescript "1.7.170" :scope "provided"]
                 [io.pedestal/pedestal.service "0.4.1"]

                 ;; Remove this line and uncomment one of the next lines to
                 ;; use Immutant or Tomcat instead of Jetty:
                 [io.pedestal/pedestal.jetty "0.4.1"]
                 [figwheel-sidecar "0.5.0-2" :exclusions [clj-time joda-time org.clojure/tools.reader] :scope "test"]

                 ;; [io.pedestal/pedestal.immutant "0.4.1"]
                 ;; [io.pedestal/pedestal.tomcat "0.4.1"]

                 [org.omcljs/om "1.0.0-alpha28"]

                 [ch.qos.logback/logback-classic "1.1.3" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.12"]
                 [org.slf4j/jcl-over-slf4j "1.7.12"]
                 [org.slf4j/log4j-over-slf4j "1.7.12"]]

  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  :figwheel {:build-ids ["dev" "tempid-test"]}
  :cljsbuild {
              :builds
              [
               {:id           "dev"
                :figwheel     true
                :source-paths ["src/main"]
                :compiler     {:main                 tempid-test.core
                               :asset-path           "/js/out"
                               :output-to            "resources/public/js/main.js"
                               :output-dir           "resources/public/js/out"
                               :recompile-dependents true
                               :parallel-build       true
                               :verbose              false}}

               {:id           "tempid-test"
                :figwheel     true
                :source-paths ["cljs-src"]
                :compiler     {
                               :main                 tempid-test.core
                               :source-map-timestamp true
                               :asset-path           "/js/out"
                               :output-to            "resources/public/js/main.js"
                               :output-dir           "resources/public/js/out"
                               :parallel-build       true
                               :recompile-dependents true
                               :verbose              false}}]}
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "tempid-test.server/run-dev"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.4.1"]]}
             :uberjar {:aot [tempid-test.server]}}
  :main ^{:skip-aot true} tempid-test.server)

