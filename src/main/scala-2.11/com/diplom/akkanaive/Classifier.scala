package com.diplom.akkanaive

/**
  * Алгоритм классификации
  * @param model статистическая модель классификатора
  */
class Classifier(model: Model) {

  /**
    * Разбивает строку на аттрибуты
    * @param str строка
    * @return
    */
  def tokenize(str: String) = str.split(':')

  /**
    * Рассчитывает оценку вероятности в пределах класса
    * @param clazz класс
    * @param str строка
    * @return оценка <code>P(c|d)</code>
    */
  def calculateProbability(clazz: String, str: String) = {
    tokenize(str).map(model.attrLogProbability(clazz, _)).sum + model.classLogProbability(clazz)
  }
}
