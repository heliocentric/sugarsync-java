/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.sugarsync;

/**
 *
 * @author helio
 */
public class SugarsyncJava {

	/**
	 * @param args the command line arguments
	 */

	public static void main(String[] args) {
		
		SugarSyncAPI main = new SugarSyncAPI(args[0], args[1], args[2]);
		main.start();
	}
}
