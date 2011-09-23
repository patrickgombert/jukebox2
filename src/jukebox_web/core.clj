(ns jukebox-web.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [jukebox-player.core :as player]
            [jukebox-web.models.db :as db]
            [jukebox-web.models.playlist :as playlist]
            [jukebox-web.controllers.hammertimes :as hammertimes-controller]
            [jukebox-web.controllers.library :as library-controller]
            [jukebox-web.controllers.playlist :as playlist-controller]
            [jukebox-web.controllers.player :as player-controller]
            [jukebox-web.controllers.users :as users-controller]))

(defroutes main-routes
  (GET "/" [] {:status 302 :headers {"Location" "/playlist"}})
  (GET "/playlist" [] playlist-controller/index)
  (GET "/playlist/add-one" [] playlist-controller/add-one)
  (POST "/playlist/add" [] playlist-controller/add)
  (GET ["/playlist/add/:song" :song #".*"] [] playlist-controller/add)
  (GET "/player/play" [] player-controller/play)
  (GET "/player/pause" [] player-controller/pause)
  (GET "/player/skip" [] player-controller/skip)
  (GET "/users" [] users-controller/index)
  (GET "/users/sign-in" [] users-controller/sign-in)
  (POST "/users/sign-out" [] users-controller/sign-out)
  (POST "/users/authenticate" [] users-controller/authenticate)
  (GET "/users/sign-up" [] users-controller/sign-up-form)
  (POST "/users/sign-up" [] users-controller/sign-up)
  (POST "/users/toggle-enabled" [] users-controller/toggle-enabled)
  (GET "/hammertimes" [] hammertimes-controller/index)
  (GET "/hammertime" [] hammertimes-controller/create-form)
  (POST "/hammertime" [] hammertimes-controller/create)
  (POST "/hammertimes/play" [] hammertimes-controller/play)
  (POST "/library/upload/:user" [] library-controller/upload)
  (GET "/library/browse" [] library-controller/browse-root)
  (GET ["/library/browse/:path", :path #".*"] [] library-controller/browse)
  (route/resources "/")
  (route/not-found "Page not found"))

(player/start (playlist/playlist-seq))

(defn with-connection [handler]
  (fn [request]
    (let [connection (db/open-db "data/jukebox.fdb")
          response (binding [db/*db* connection] (handler request))]
      (db/close-db connection)
      response)))

(def app
  (-> (handler/site main-routes)
      (with-connection)))
