(ns jukebox-web.views.users
  (:require [jukebox-web.views.layout :as layout])
  (:use [hiccup core page-helpers form-helpers]
        [jukebox-web.views.forms]))

(defn sign-up [request errors]
  (layout/main request "Sign Up"
    (form-to [:post "/users/sign-up"]
      (labeled-field text-field :login "Login" errors)
      (labeled-field password-field :password "Password" errors)
      (labeled-field password-field :password_confirmation "Password Confirmation" errors)
      (labeled-field text-field :avatar "Avatar" errors)
      [:div (submit-button "Sign Up")])))

(defn- show-user [user]
  [:tr
    [:td [:img {:src (:avatar user)}]]
    [:td [:span (:login user)]]
    [:td [:span (:skip-count user)]]
    [:td (form-to [:post "/users/toggle-enabled"]
           (hidden-field "login" (:login user))
           (submit-button (if (:enabled user) "Disable" "Enable")))]])

(defn index [request users]
  (layout/main request "Users"
    [:table
      [:tr [:th] [:th "Login"] [:th "Skips"]]
      (map show-user users)]))
