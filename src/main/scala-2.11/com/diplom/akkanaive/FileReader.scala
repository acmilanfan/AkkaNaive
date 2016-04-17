package com.diplom.akkanaive

import java.io.{IOException, FileNotFoundException}

import scala.io.Source


class FileReader {
  def readAttrs(fileName: String): String = {
    try {
      val fileLines = Source.fromFile(fileName).getLines().toList.last
      return fileLines
    } catch {
      case ex: FileNotFoundException => println("Файл не найден")
        return ""
      case ex: IOException => println("Ошибка ввода/вывода")
        return ""
    }
  }

  def read(fileName: String, learning: Learning) = {
    try {
      val fileLines = Source.fromFile(fileName).getLines().toList
      for (line <- fileLines) {
        learning.addExample(line.substring(0, line.lastIndexOf(':') - 1), line.substring(line.lastIndexOf(':') + 1, line.length))
      }
    } catch {
      case ex: FileNotFoundException => println("Файл не найден")
      case ex: IOException => println("Ошибка ввода/вывода")
    }
  }
}
