package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {

	@Test
	public void testTheTruth() {
		assertTrue(true);
	}
	@Test
	public void exampleTest() {
		//create an inn, add an item, and simulate one day
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		inn.oneDay();
		
		//access a list of items, get the quality of the one set
		List<Item> items = inn.getItems();
		int quality = items.get(0).getQuality();

		//assert quality has decreased by one
		assertEquals("Failed quality for Dexterity Vest", 19, quality);
	}
	
	@Test
	public void testMain(){
		ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outPutStream));
		//ACT
        GildedRose.main(new String[]{});
        System.setOut(System.out);
                
        assertEquals("Failed main function output", "OMGHAI!",outPutStream.toString().trim());
	}
	
	@Test
	public void decreasingQualityTest() {
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Conjured Mana Cake", 5, 5));
		inn.setItem(new Item("Conjured Mana Cake", -1, 3));
		inn.setItem(new Item("+5 Dexterity Vest", 10, 20));
		inn.setItem(new Item("+5 Dexterity Vest", -1, 20));
		
		//Act
		inn.oneDay();
		
		//Qualities should be as follows:
		/* 1. mana cake: 3
		 * 2. mana cake: 0
		 * 3. Dexterity Vest, 19
		 * 4. Dexterity Vest,  18 */
		
		List<Item> items = inn.getItems();
		assertEquals("The quality is not valid for the item", 4, items.get(0).getQuality());
		assertEquals("The quality is not valid for the item", 1, items.get(1).getQuality());
		assertEquals("The quality is not valid for the item", 19, items.get(2).getQuality());
		assertEquals("The quality is not valid for the item", 18, items.get(3).getQuality());
	}
	
	@Test 
	public void NegativeQualityTest() {
		//create a inn, add a nonSpecial item
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Conjured Mana Cake", 0, 0));
		
		//simulating one day
		inn.oneDay();
		inn.oneDay();
		//After this the values should be -2, 0 as quality cannot decrease
		
		List<Item> items = inn.getItems();
		
		assertEquals("Quality should not be negative", 0, items.get(0).getQuality());
		assertEquals("SellIn value is not valid", -2, items.get(0).getSellIn());
	}
	
	@Test
	public void testSulfuras(){
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Sulfuras, Hand of Ragnaros", 0, 80));
		inn.oneDay();
		inn.oneDay();
		inn.oneDay();
		inn.oneDay();
		
		List<Item> items = inn.getItems();
		
		assertEquals("The sellinvalue should not decrease from 0", 0, items.get(0).getSellIn());
		assertEquals("Sulfuras quality cannot decrease", 80, items.get(0).getQuality());
	}
	
	@Test
	public void testAgedBrie(){
		//* "Aged Brie" actually increases in Quality the older it gets
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Aged Brie", -1, 40));
		inn.setItem(new Item("Aged Brie", -2, 45));
		inn.setItem(new Item("Aged Brie", 0, 30));
		inn.oneDay();
		
		//Qualities should be as follows:
				/* 1.aged brie: 41
				 * 2.aged brie: 46
				 * 3.aged brie: 31 */
		
		List<Item> items = inn.getItems();
		assertEquals("The quality is not valid for the item", 42, items.get(0).getQuality());
		assertEquals("The quality is not valid for the item", 47, items.get(1).getQuality());
		assertEquals("The quality is not valid for the item", 32, items.get(2).getQuality());
	}

	@Test
	public void testBackStagePass(){
	/* * "Backstage passes", like aged brie, increases in Quality as it's SellIn value approaches;
	Quality increases by 2 when there are 10 days or less and by 3 when there are 5 days or
	less but Quality drops to 0 after the concert.*/
		GildedRose inn = new GildedRose();
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 7, 30));
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 4, 30));
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 0, 50));

		inn.oneDay();
		//qualities should be as follows
		/* 1. pass: 32
		 * 2. pass: 33
		 * 3. pass: 0   */
		List<Item> items = inn.getItems();
		assertEquals("The quality is not valid for the item", 32, items.get(0).getQuality());
		assertEquals("The quality is not valid for the item", 33, items.get(1).getQuality());
		assertEquals("The quality is not valid for the item", 0, items.get(2).getQuality());
	}
	
	@Test
	public void QualityIsNeverMoreThan50() {
		GildedRose inn = new GildedRose();
		
		//* "Aged Brie" actually increases in Quality the older it gets
		//Backstage passes", like aged brie, increases in Quality as it's SellIn value approaches;
		//Quality increases by 2 when there are 10 days or less and by 3 when there are 5 days or
		//less but Quality drops to 0 after the concert.
		//* The Quality of an item is never more than 50

		inn.setItem(new Item("Aged Brie", 0, 50));
		inn.setItem(new Item("Aged Brie", -1, 49));
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 7, 49));
		inn.setItem(new Item("Backstage passes to a TAFKAL80ETC concert", 3, 49));
		
		inn.oneDay();
		inn.oneDay();
		//All of the qualities should be 50
		List<Item> items = inn.getItems();
		assertEquals("The quality is not valid for the item", 50, items.get(0).getQuality());
		assertEquals("The quality is not valid for the item", 50, items.get(1).getQuality());
		assertEquals("The quality is not valid for the item", 50, items.get(2).getQuality());
		assertEquals("The quality is not valid for the item", 50, items.get(3).getQuality());
		
	}
	
}
