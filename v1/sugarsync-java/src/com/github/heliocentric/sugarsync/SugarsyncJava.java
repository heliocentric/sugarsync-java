/*
 * 
 * Copyright (c) 2012, Dylan Cochran <heliocentric@gmail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any purpose
 * with or without fee is hereby granted, provided that the above copyright 
 * notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY 
 * AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, 
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM 
 * LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR 
 * PERFORMANCE OF THIS SOFTWARE.
 * 
 * 
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
		PropertyList options = new PropertyList();
		options.put("backend","SugarSync");
		switch (args.length) {
			case 5:
				options.put("PrivateKey",args[4]);
			case 4:
				options.put("KeyID",args[3]);
			case 3:
				options.put("AppID",args[2]);
			case 2:
				options.put("Password",args[1]);
			case 1:
				options.put("Username",args[0]);
		}
		Synchro main = new Synchro(options);
		main.start();
	}
}
