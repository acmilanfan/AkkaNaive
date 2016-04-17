package com.diplom.akkanaive

import akka.actor.{ActorSystem, Props}
import akka.routing.RoundRobinPool
import com.diplom.akkanaive.actors.{ClassifierActor, ControllerActor}

import scala.io.StdIn

case object StartMsg
case object StopMsg
case object WrongMsg
case class LearnMsg(attrs: String, clazz: String, learning: Learning)
case class ClassifyMsg(attrs: String, learning: Learning)
case class CalculateClassResultMsg(clazz: String, result: Double, learning: Learning)
case class CalculateClassMsg(clazz: String, attrs: String, learning: Learning)

object Main extends App {

  val learning = new Learning()
  val fileReader = new FileReader()
  var input = ""

  //init actors
  val system = ActorSystem("NaiveSystem")
  val classifierActor = system.actorOf(Props[ClassifierActor].withRouter(RoundRobinPool(3)), name = "classifiers")
  val controllerActor = system.actorOf(Props(new ControllerActor(classifierActor)), name = "controller")

  def printMenu() = {
    println("Введите 1 для добавления обучающей строки")
    println("Введите 2 для классификации")
    println("Введите q для выхода")
  }

  controllerActor ! StartMsg

  if (args.length != 0 && args(0) == "-h") {
    while (input != "q") {
      printMenu()
      input = StdIn.readLine()
      input match {
        case "1" =>
          println("Введите атрибуты")
          val str = StdIn.readLine()
          println("Введите класс")
          val clazz = StdIn.readLine()
          controllerActor ! LearnMsg(str, clazz, learning)
        case "2" =>
          println("Введите атрибуты")
          val str = StdIn.readLine()
          controllerActor ! ClassifyMsg(str, learning)
        case "q" =>
          controllerActor ! StopMsg
          system.terminate()
        case _ =>
          println("Некорректный ввод")
      }
    }
  } else if (args.length < 3) {
    println("Некорректные параметры запуска программы.")
    println("Необходимо использовать данный формат: <AtrPath> <Object Name> <Data Path>")
  } else {
    fileReader.read(args(2), learning)
    controllerActor ! ClassifyMsg(fileReader.readAttrs(args(0)), learning)
  }
}
