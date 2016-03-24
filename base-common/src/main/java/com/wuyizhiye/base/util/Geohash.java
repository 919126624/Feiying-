package com.wuyizhiye.base.util;



import java.util.BitSet;
import java.util.HashMap;

/**
 * 用字符串实现附近地点搜索
 * 
 * @author li.biao 
 * @desc 
 * 1、geohash用一个字符串表示经度和纬度两个坐标
 * 2、 geohash表示的并不是一个点，而是一个矩形区域
 * 3、编码的前缀可以表示更大的区域。例如wx4g0ec1，它的前缀wx4g0e表示包含编码wx4g0ec1在内的更大范围。这个特性可以用于附近地点搜索 
 * 4、参考api:http://blog.charlee.li/geohash-intro/
 */
public class Geohash {

	private static int numbits = 6 * 5;
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();
	static {
		int i = 0;
		for (char c : digits)
			lookup.put(c, i++);
	}

	/**
	 * 解码geohash字符串为坐标
	 * 
	 * @param geohash
	 * @return
	 */
	public static double[] decode(String geohash) {
		StringBuilder buffer = new StringBuilder();
		for (char c : geohash.toCharArray()) {

			int i = lookup.get(c) + 32;
			buffer.append(Integer.toString(i, 2).substring(1));
		}

		BitSet lonset = new BitSet();
		BitSet latset = new BitSet();

		// even bits
		int j = 0;
		for (int i = 0; i < numbits * 2; i += 2) {
			boolean isSet = false;
			if (i < buffer.length())
				isSet = buffer.charAt(i) == '1';
			lonset.set(j++, isSet);
		}

		// odd bits
		j = 0;
		for (int i = 1; i < numbits * 2; i += 2) {
			boolean isSet = false;
			if (i < buffer.length())
				isSet = buffer.charAt(i) == '1';
			latset.set(j++, isSet);
		}
		// 中国地理坐标：东经73°至东经135°，北纬4°至北纬53°
		double lon = decode(lonset, 70, 140);
		double lat = decode(latset, 0, 60);

		return new double[] { lat, lon };
	}

	private static double decode(BitSet bs, double floor, double ceiling) {
		double mid = 0;
		for (int i = 0; i < bs.length(); i++) {
			mid = (floor + ceiling) / 2;
			if (bs.get(i))
				floor = mid;
			else
				ceiling = mid;
		}
		return mid;
	}

	/**
	 * 加密坐标为geohash码
	 * 
	 * @param lat 纬度
	 * @param lon 经度
	 * @return
	 */
	public static String encode(double lat, double lon) {
		BitSet latbits = getBits(lat, 0, 60);
		BitSet lonbits = getBits(lon, 70, 140);
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < numbits; i++) {
			buffer.append((lonbits.get(i)) ? '1' : '0');
			buffer.append((latbits.get(i)) ? '1' : '0');
		}
		return base32(Long.parseLong(buffer.toString(), 2));
	}

	/**
	 * 截取geohash码去比较，level越大表示越临近
	 * 
	 * @param lat 纬度
	 * @param lon 经度
	 * @param level 模糊值
	 * @return
	 */
	public static String encode(double lat, double lon, int level) {
		level = level < 0 ? 0 : level;
		level = level > 12 ? 12 : level;
		String geohash = encode(lat, lon);
		if(geohash == null || "".equals(geohash)){
			geohash = "000000000000" ;
		}
		return geohash.length() <= level ? geohash : geohash.substring(0, level);
	}

	private static BitSet getBits(double lat, double floor, double ceiling) {
		BitSet buffer = new BitSet(numbits);
		for (int i = 0; i < numbits; i++) {
			double mid = (floor + ceiling) / 2;
			if (lat >= mid) {
				buffer.set(i);
				floor = mid;
			} else {
				ceiling = mid;
			}
		}
		return buffer;
	}

	public static String base32(long i) {
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (i < 0);
		if (!negative)
			i = -i;
		while (i <= -32) {
			buf[charPos--] = digits[(int) (-(i % 32))];
			i /= 32;
		}
		buf[charPos] = digits[(int) (-i)];

		if (negative)
			buf[--charPos] = '-';
		return new String(buf, charPos, (65 - charPos));
	}

	public static void main(String[] args) {
		Geohash geo = new Geohash();
		// 三诺大厦22.52782, 113.943645
		String a = geo.encode(22.52782, 113.943645);
//		System.out.println(a);
//		System.out.println(geo.encode(22.52782, 113.943645, 5));
//		System.out.println("-----------------------");
		// 腾讯大厦22.529236, 113.941369
		String b = geo.encode(22.529236, 113.941369);
//		System.out.println(b);
//		System.out.println(geo.encode(22.529236,113.941369, 5));
//		System.out.println("-----------------------");
		// 芒果网大厦22.529448,113.941473
		String c = geo.encode(22.529448,113.941473);
//		System.out.println(c);
//		System.out.println(geo.encode(22.529448,113.941473, 5));
//		System.out.println("-----------------------");
		//22.526876,113.931114
		String d = geo.encode(22.526876,113.931114);
//		System.out.println(d);
//		System.out.println(geo.encode(22.526876,113.931114, 5));
	}
}
