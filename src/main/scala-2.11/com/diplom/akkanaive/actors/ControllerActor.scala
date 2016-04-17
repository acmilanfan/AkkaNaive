package com.diplom.akkanaive.actors

import akka.actor.{ActorRef, Actor}
import com.diplom.akkanaive._

/**
  * Created by andrey on 07/01/16.
  */
class ControllerActor(classifier: ActorRef) extends Actor {

  private var results: List[(String, Double)] = List()
  private var classesCount: Int = 0

  var t0: Long = 0

  def receive = {
    case StartMsg =>
      println("Добро пожаловать в систему")
    case StopMsg =>
      println("Завершение работы с программой")
      classifier ! StopMsg
      context.stop(self)
    case LearnMsg(attrs, clazz, learning) =>
      learning.addExample(attrs, clazz)
      classesCount = learning.model.classes.size
      println("Строка добавлена")
    case ClassifyMsg(attrs, learning) =>
      classesCount = learning.model.classes.size
      println(s"Количество классов - $classesCount")
      t0 = System.currentTimeMillis()
      learning.model.classes.foreach(clazz => classifier ! CalculateClassMsg(clazz, attrs, learning))
    case CalculateClassResultMsg(clazz, result, learning) =>
      results = (clazz, result) :: results
      if (results.size == classesCount) {
        val t1 = System.currentTimeMillis()
        println(s"Время работы - ${t1-t0}")
        results = results.sortBy(_._2)
        println(s"Наиболее вероятный класс - ${results.last._1}")
        println(s"Вероятность - ${learning.normProb(results, results.last._2)}")
        self ! StopMsg
      }
    case WrongMsg => println("Получено некорректное сообщение")
    case _ => sender ! WrongMsg
  }
}
