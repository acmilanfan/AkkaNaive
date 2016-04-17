package com.diplom.akkanaive

/**
  * Обучающий алгоритм классификации
  */
class Learning {

  private var examples: List[(String, String)] = List()

  private val tokenize = (str: String) => str.split(':')
  private val tokenizeTuple = (str: (String, String)) => tokenize(str._1)

  /**
    * Добавление примера
    * @param exStr строка
    * @param clazz класс
    */
  def addExample(exStr: String, clazz: String) {
    examples = (exStr, clazz) :: examples
  }

  def dictionary = examples.map(tokenizeTuple).flatten.toSet

  def model = {
    val objByClass = examples.groupBy(_._2)
    val objCountsMap = objByClass.mapValues(_.length)
    val attrsMap = objByClass.mapValues(_.map(tokenizeTuple).flatten.groupBy(x => x).mapValues(_.length))

    new Model(objCountsMap, attrsMap, dictionary.size)
  }

  def classifier = new Classifier(model)

  def normProb(probs: List[(String, Double)], prob: Double) = {
    math.exp(prob) / probs.map(t => math.exp(t._2)).sum
  }
}
