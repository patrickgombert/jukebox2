(ns jukebox-web.views.hammertimes
  (:require [jukebox-web.views.layout :as layout])
  (:use [hiccup core form-helpers page-helpers]
        [jukebox-web.views.forms]))

(defn create [request errors]
  (layout/main request "Create Hammertime"
     (form-to [:post "/hammertime"]
       (labeled-field text-field :name "Name" errors)
       (labeled-field text-field :file "File" errors)
       (labeled-field text-field :start "Start" errors)
       (labeled-field text-field :end "End" errors)
       (submit-button "Create"))))

(defn- show-hammertime [hammertime]
  [:tr
    [:td (:name hammertime)]
    [:td
      (form-to [:post "/hammertimes/play"]
        (hidden-field :name (:name hammertime))
        (submit-button "Play"))]])

(defn index [request hammertimes]
  (layout/main request "Hamertimes"
    [:table
      [:tr [:th "Name"] [:th]]
      (map show-hammertime hammertimes)]
    [:a {:href "/hammertime"} [:button.btn.success "Add"]]))
