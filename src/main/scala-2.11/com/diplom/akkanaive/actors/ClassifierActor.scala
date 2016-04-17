package com.diplom.akkanaive.actors

import akka.actor.Actor
import com.diplom.akkanaive.{CalculateClassResultMsg, CalculateClassMsg, StopMsg, WrongMsg}

/**
  * Created by andrey on 07/01/16.
  */
class ClassifierActor extends Actor{

  def receive = {
    case CalculateClassMsg(clazz, attrs, learning) =>
      val prob = learning.classifier.calculateProbability(clazz, attrs)
      sender ! CalculateClassResultMsg(clazz, prob, learning)
    case StopMsg =>
      context.stop(self)
      context.system.terminate()
    case WrongMsg => println("Получено некорректное сообщение")
    case _ => sender ! WrongMsg
  }
}
