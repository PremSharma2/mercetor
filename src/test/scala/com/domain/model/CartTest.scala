package com.domain.model

import com.domain.filter.Offers
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.collection.concurrent.TrieMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

@RunWith(classOf[JUnitRunner])
class CartTest extends FunSuite with BeforeAndAfter with Matchers {

  private val inventoryList = TrieMap("Apple" -> BigDecimal(0.60), "Oranges" -> BigDecimal(0.25))

  private val orderList= new ArrayBuffer[String] with mutable.SynchronizedBuffer[String]
  orderList +=( "Apple", "Oranges", "Apple" , "Oranges", "Oranges")


  val cart = new Cart(inventoryList)

  test("Test the price per ordered item") {

    val list = cart.getOrderPricing(orderList).asInstanceOf[ArrayBuffer[BigDecimal]].toList
    assert(list ==  List(0.6, 0.25, 0.6, 0.25, 0.25))
  }



  test("Test total cost") {

    assert(cart.getTotal(orderList) == 1.95)
  }

  test("Test total cost after discounts applied") {

    val offers = new Offers(Map(("Apple" -> 2), ("Oranges" -> 3) ))
    assert(cart.getTotal(offers.currentOffers(orderList,inventoryList)) == 1.10)


  }

}
