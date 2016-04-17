package com.diplom.akkanaive

import scala.math.log

/**
  * Модель классификатора
  *
  * @param objCountMap количество объектов по класам
  * @param attrMap статистика по аттрибутам в пределах классов
  * @param dictionarySize размер словаря по аттрибутам обуающей выборки
  */
class Model(objCountMap: Map[String, Int],
            attrMap: Map[String, Map[String, Int]],
            dictionarySize: Int) {

  /**
    * @param clazz класс
    * @param attr слово
    * @return логарифм оценки <code>P(w|c)</code> — вероятности атрибута в пределах класса
    */
  def attrLogProbability(clazz: String, attr: String) =
    log((attrMap(clazz).getOrElse(attr, 0) + 1.0) / dictionarySize)

  /**
    * @param clazz класс
    * @return логарифм априорной вероятности класса <code>P(c)</code>
    */
  def classLogProbability(clazz: String) = log(objCountMap(clazz).toDouble / objCountMap.values.sum)

  /**
    * @return множество всех классов
    */
  def classes = objCountMap.keySet

}
