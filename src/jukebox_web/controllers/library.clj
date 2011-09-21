(ns jukebox-web.controllers.library
  (:import [java.io File]
           [java.util UUID])
  (:require [clojure.java.io :as io]
            [clojure.string :as s])
  (:use [jukebox-player.tags]))

(def *music-library* "music")

(defn- file-path [& parts]
  (s/join File/separator parts))

(defn- mkdir-p [path]
  (.mkdirs (io/as-file path)))

(defn- mv [from to]
  (.renameTo (io/as-file from) (io/as-file to)))

(defn- extension [filename]
  (last (s/split (str filename) #"\.")))

(defn- rename-with-tags [user file]
  (let [{:keys [artist album title]} (extract-tags file)
        dir (file-path *music-library* user artist album)
        new-file (file-path dir (str title "." (extension file)))]
    (mkdir-p dir)
    (mv file new-file)))

(defn- save-file [tempfile user ext]
  (let [file-with-ext (io/as-file (file-path *music-library* (str (UUID/randomUUID) "." ext)))]
    (io/copy tempfile file-with-ext)
    (rename-with-tags user file-with-ext)))

(defn upload [request]
  (let [{:keys [tempfile filename]} (-> request :params :file)
        user (-> request :params :user)]
    (save-file tempfile user (extension filename)))
  "upload complete")
