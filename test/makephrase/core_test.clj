(ns makephrase.core-test
  (:require [clojure.test :refer :all]
            [makephrase.core :refer :all]))

(deftest test-apply-to-random-element
  (is (empty? (apply-to-random-element identity [])))
  (is (= 1 (count (filter identity
                          (apply-to-random-element complement [false false false false]))))))

(deftest test-index-nth-if
  (is (= 0 (index-nth-if [:a :b :c] (constantly true) 0)))
  (is (= 2 (index-nth-if [:a :b :c] (constantly true) 2)))
  (is (nil? (index-nth-if [:a :b :c] (constantly true) 4)))
  (is (= 0 (index-nth-if [:a :b :c :d] (constantly true) 0)))
  (is (nil? (index-nth-if [] (constantly false) 0)))
  (is (= 2 (index-nth-if [:b :a :a :a] #{:a}  1)))
  (is (nil? (index-nth-if [:a :a :a :a] #{:a} 4))))

(deftest test-studlify
  (is (empty? (studlify "")))
  (is (= "a" (studlify "a")))
  (is (= "aB"(studlify "ab")))
  (is (= "AB"(studlify "AB")))
  (is (let [s "1111111"] (= s (studlify s))))
  (is (= "aB1Ab" (studlify "ab1ab"))))

(deftest test-add-cap
  (is (empty? (add-cap "")))
  (is (= 1 (count (filter #(Character/isUpperCase %) (add-cap "foobarbaz")))))
  (is (let [s "012,.!@#$%^&*()"] (= s (add-cap s)))))

(deftest test-nth-random-occurrence
  (is (nil? (nth-random-occurrence [] :anything)))
  (is (nil? (nth-random-occurrence [:a :a :a] :not-a)))
  (is (= 0 (nth-random-occurrence [:a] :a)))
  (is (let [coll [:a :c :d :e]] (= (dec (count coll)) (nth-random-occurrence coll (last coll)))))
  (is (#{0 3 4} (nth-random-occurrence [:a nil nil :a :a nil nil] :a))))

