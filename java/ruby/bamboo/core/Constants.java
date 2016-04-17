package ruby.bamboo.core;

/**
 * てきとーな定数
 *
 * @author Ruby
 *
 */
public class Constants {

	public static final String MODID = "BambooMod";
	public static final String MC_VER = "@MC_VERSION@";
	public static final String BAMBOO_VER = "@VERSION@";
	public static final String RESOURCED_DOMAIN = "bamboomod:";
	public static final String STR_EMPTY = "";

	//すてーとたいぷ(良くわかってない)
	public static final String META = "meta";
	public static final String TYPE = "type";
	public static final String AGE = "age";
    public static final String FLG = "flg";

	//せぱれー
	/**
	 * ドメイン変わることもある？
	 */
	public static final String DMAIN_SEPARATE = ":";
	/**
	 * ぱっけーじ用
	 */
	public static final String PACKAGE_SEPARATE = ".";

	// ぱす
	/**
	 * パッケージパス
	 */
	public static final String BLOCK_PACKAGE = "ruby.bamboo.block";
	public static final String ITEM_PACKAGE = "ruby.bamboo.item";

	public static String getModDomain(){
	    return MODID+DMAIN_SEPARATE;
	}

	public static String getBlockTexPath(){
	    return getModDomain()+"blocks/";
	}

	   public static String getItemTexPath(){
	        return getModDomain()+"items/";
	    }
}
