(ns makephrase.core-test
  (:require [clojure.test :refer :all]
            [makephrase.core :refer :all]))

(deftest test-index-nth-if
  (is (= 0 (index-nth-if [:a :b :c] (constantly true) 0)))
  (is (= 2 (index-nth-if [:a :b :c] (constantly true) 2)))
  (is (nil? (index-nth-if [:a :b :c] (constantly true) 4)))
  (is (= 0 (index-nth-if [:a :b :c :d] (constantly true) 0)))
  (is (nil? (index-nth-if [] (constantly false) 0)))
  (is (= 2 (index-nth-if [:b :a :a :a] #(= :a %) 1)))
  (is (nil? (index-nth-if [:a :a :a :a] #(= :a %) 4))))

(deftest test-apply-to-random-element
  (is (= 1 (count (filter identity
                          (apply-to-random-element complement [false false false false]))))))
