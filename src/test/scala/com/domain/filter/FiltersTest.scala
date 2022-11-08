package com.domain.filter

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

import scala.collection.concurrent.TrieMap
import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[JUnitRunner])
class FiltersTest extends FunSuite with BeforeAndAfter with Matchers {

  val offers =  Offers(Map(("Apple" -> 2),  ("Oranges" -> 3) ))
  private val inventoryList = TrieMap("Apple" -> BigDecimal(0.60), "Oranges" -> BigDecimal(0.25))

  test("Offers applied on one Apple and one Orange (nothing should be changed)") {

    val oneItemEach = ArrayBuffer("Apple", "Oranges")
    var afterOffer = offers.currentOffers(oneItemEach,inventoryList)
    assert(afterOffer == ArrayBuffer("Apple","Oranges"))
  }

  test("Offers applied on one each product in basket even though we've a few Apple and oranges (noting should be changed)") {

    val order = ArrayBuffer ( "Apple", "Oranges", "Apple", "Oranges", "Oranges")
    var afterOffer = offers.currentOffers(order,inventoryList)
    assert(afterOffer == ArrayBuffer("Apple", "Oranges", "Oranges"))
  }

  test("Two Apple as buy one get one free () ") {

    val order = ArrayBuffer("Apple", "Apple")
    var afterOffer = offers.currentOffers(order,inventoryList)
    assert(afterOffer == ArrayBuffer("Apple"))
  }

  test("Three Oranges for price of two (final ArrayBuffer should have two Oranges)") {

    val order = ArrayBuffer("Oranges", "Oranges", "Oranges")
    var afterOffer = offers.currentOffers(order,inventoryList)
    assert(afterOffer == ArrayBuffer("Oranges", "Oranges"))
  }

  test("Two Apple as buy one get one free and three Oranges for price of two ") {

    val order = ArrayBuffer("Oranges", "Oranges", "Apple", "Oranges", "Apple")
    var afterOffer = offers.currentOffers(order,inventoryList)
    assert(afterOffer == ArrayBuffer("Oranges", "Oranges", "Apple"))
  }

  test("Testing empty ArrayBuffer ") {
    assert(offers.currentOffers(ArrayBuffer(),inventoryList) == ArrayBuffer())
  }

}
