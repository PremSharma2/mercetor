package com.domain.filter

import scala.collection.concurrent.TrieMap
import scala.collection.{immutable, mutable}
import scala.collection.mutable.ArrayBuffer

sealed trait Filters

case class Offers(productToOffer: Map[String, Int]) extends Filters {

  def currentOffers(order: ArrayBuffer[String], inventoryList: TrieMap[String,BigDecimal]): ArrayBuffer[String] = {

    val productGroup = new scala.collection.concurrent.TrieMap[String, Int]()
    // product group will be populated as
    // it is nothing but frquency of elements
    // val productGroup=TrieMap(Apple -> 2, Banana -> 1, Lime -> 3, Melon -> 2)
    //products under  offer are : ->
    // val offers = new Offers(Map(("Melon" -> 2), ("Lime" -> 3)))
    val dropList = new mutable.HashSet[String]() with mutable.SynchronizedSet[String]
// will add items are under offer to this List
   val map: immutable.Iterable[Option[Int]] = order.groupBy(item => item).map(x => productGroup.put(x._1, x._2.length))

    productGroup.foreach { x =>

      if (x._2 > 1) {

        if (null != productToOffer.get(x._1).orNull) {
// offer key is 2 for apple i.e fetching the offer key from offer Map
          val offerKey = productToOffer.getOrElse(x._1, 0)

          offerKey match {

            //Buy one get one free
            case (2) =>

              if (productGroup.getOrElse(x._1, 0) % 2 == 0 || productGroup.getOrElse(x._1, 0) - 1 > 3) {
                val j = productGroup.getOrElse(x._1, 0) / 2
                for (i <- 1.to(j)) {
                  dropList += x._1
                }
              }

            //Three for the price two for Lime
            case (3) => {

              if (productGroup.getOrElse(x._1, 0) % 2 != 0 || productGroup.getOrElse(x._1, 0) > 4) {
                val j = productGroup.getOrElse(x._1, 0) / 3
                for (i <- 1.to(j)) {
                  dropList += x._1
                }
              }
            }
            case _ => Nil
          }
        }
      }
    }

   order diff dropList.toList

  }
}

