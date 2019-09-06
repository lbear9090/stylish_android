package com.stylelist.Utils;

import com.stylelist.R;

/**
 * Created by security on 3/1/2018.
 */

public class Constants {

    public static final String FOR_SALE = "For Sale";
    public static final String FOR_HIRE = "For Hire";
    public static final String FOR_INSPIRATION = "For Inspiration";

    public static final String FOLLOW = "follow";
    public static final String COMMENT = "comment";
    public static final String LIKE = "like";

    public static final String PARENT_ACTIVITY = "parent";
    public static final int PARENT_IS_PROFILE = 0;
    public static final int PARENT_IS_PHOTO = 1;
    public static final int PARENT_IS_OTHER_PROFILE = 2;

    public static final String POST_STYLE_TAG = "Styles";
    public static final String POST_HASH_TAGS = "HashTags";
    public static final String POST_SALE_TYPE = "SaleType";
    public static final String POST_CONDITION = "Condition";
    public static final String POST_LOCATION_COUNTRY = "LocationCountryInfo";
    public static final String POST_LOCATION_CITY = "LocationCityInfo";
    public static final String POST_POPULAR_POINTS = "popularPoints";

    public static final String ITEM_POST_HASH_TAG = "HashTag";
    public static final String ITEM_POST_SALE_TYPE = "SaleType";
    public static final String ITEM_POST_CONDITION_DETAIL = "ConditionDetail";
    public static final String ITEM_POST_HASH_TAG_SIZE = "HashTagSize";
    public static final String ITEM_POST_LOCATION_COUNTRY_INFO = "LocationCountryInfo";
    public static final String ITEM_POST_LOCATION_CITY_INFO = "LocationCityInfo";
    public static final String ITEM_POST_HASH_TAG_DESCRIPTION = "HashTagDescription";
    public static final String ITEM_POST_DELIVERY = "Delivery";
    public static final String ITEM_POST_DELIVERYSHIPPING = "DeliveryShipping";
    public static final String ITEM_POST_DELIVERYPERSON = "DeliveryMeet";
    public static final String ITEM_POST_ITEM_PRICE = "ItemPrice";
    public static final String ITEM_POST_DEPOSIT_PRICE = "DepositPrice";
    public static final String ITEM_POST_HASH_TAG_NAME = "HashTagName";
    public static final String ITEM_POST_CONDITION = "Condition";
    public static final String ITEM_POST_IMAGE_ONE = "HashTagImgOne";
    public static final String ITEM_POST_IMAGE_TWO = "HashTagImgTwo";
    public static final String ITEM_POST_IMAGE_THREE = "HashTagImgThree";
    public static final String ITEM_POST_IMAGE_FOUR = "HashTagImgFour";
    public static final String ITEM_POST_IMAGE_FIVE = "HashTagImgFive";
    public static final String ITEM_POST_POST_ID = "PostId";
    public static final String ITEM_POST_X_POSITION = "XPosition";
    public static final String ITEM_POST_Y_POSITION = "YPosition";
    public static final String ITEM_POST_RECT_WIDTH = "RectWidth";
    public static final String ITEM_POST_RECT_HEIGHT = "RectHeight";
    public static final String ITEM_POST_VIEW_X_POSITION = "ItemViewEdgeXPosition";
    public static final String ITEM_POST_VIEW_Y_POSITION = "ItemViewEdgeYPosition";
    public static final String ITEM_POST_FILTERED_IMAGE = "FilteredImage";
    public static final String ITEM_POST_SHIPPING = "DeliveryShipping";
    public static final String ITEM_POST_MEET = "DeliveryMeet";
    public static final String ITEM_POST_DOMESTIC_COST = "DomesticCost";
    public static final String ITEM_POST_INTERNATIONAL_COST = "InternationalCost";

    public static final String[] MAN_STYLE_NAME = {
            "1970sTrend", "Biker", "BlackonBlack",
            "Businessman", "Casual", "CasualBusinessman", "Classy", "Country", "Cuban Collar",
            "Formal", "Funky", "Gangster", "Hawaiian", "Hipster", "LumberSexual",
            "Post-Soviet", "Preppy", "Retro", "Rockster", "Sexy", "Smart",
            "Sportswear", "Street", "Urban", "Utilitarian"};

    public static final int[] MAN_STYLE_DESCRIPTION = {
            R.string.m1970s_trends,
            R.string.biker,
            R.string.blackonblack,
            R.string.businessman,
            R.string.casual,
            R.string.casualbusinessman,
            R.string.classy,
            R.string.country,
            R.string.cuban_collar,
            R.string.formal,
            R.string.funky,
            R.string.gangster,
            R.string.hawaiian,
            R.string.hipster,
            R.string.lumbersexual,
            R.string.post_soviet,
            R.string.preppy,
            R.string.retro,
            R.string.rockster,
            R.string.sexy,
            R.string.smart,
            R.string.sportswear,
            R.string.street,
            R.string.urban,
            R.string.utilitarian
    };

    public static final int[][] MAN_STYLE_IMAGE = {
            {R.drawable.m1970s_trend, R.drawable.m1970s_trend2, R.drawable.m1970s_trend3},
            {R.drawable.biker, R.drawable.biker2, R.drawable.biker3},
            {R.drawable.blackonblack, R.drawable.blackonblack2, R.drawable.blackonblack3},
            {R.drawable.businessman, R.drawable.businessman2, R.drawable.businessman3},
            {R.drawable.casual, R.drawable.casual2, R.drawable.casual3},
            {R.drawable.casualbusinessman, R.drawable.casualbusinessman2, R.drawable.casualbusinessman3},
            {R.drawable.classy, R.drawable.classy2, R.drawable.classy3},
            {R.drawable.country, R.drawable.country2, R.drawable.country3},
            {R.drawable.cuban_collar, R.drawable.cuban_collar2, R.drawable.cuban_collar3},
            {R.drawable.formal, R.drawable.formal2, R.drawable.formal3},
            {R.drawable.funky, R.drawable.funky2, R.drawable.funky3},
            {R.drawable.gangster, R.drawable.gangster2, R.drawable.gangster3},
            {R.drawable.hawaiian, R.drawable.hawaiian2, R.drawable.hawaiian3},
            {R.drawable.hipster, R.drawable.hipster2, R.drawable.hipster3},
            {R.drawable.lumbersexual, R.drawable.lumbersexual2, R.drawable.lumbersexual3},
            {R.drawable.post_soviet, R.drawable.post_soviet2, R.drawable.post_soviet3},
            {R.drawable.preppy, R.drawable.preppy2, R.drawable.preppy3},
            {R.drawable.retro, R.drawable.retro2, R.drawable.retro3},
            {R.drawable.rockster, R.drawable.rockster2, R.drawable.rockster3},
            {R.drawable.sexy, R.drawable.sexy2, R.drawable.sexy3},
            {R.drawable.smart, R.drawable.smart2, R.drawable.smart3},
            {R.drawable.sportswear, R.drawable.sportswear2, R.drawable.sportswear3},
            {R.drawable.street, R.drawable.street2, R.drawable.street3},
            {R.drawable.urban, R.drawable.urban2, R.drawable.urban3},
            {R.drawable.utilitarian, R.drawable.utilitarian2, R.drawable.utilitarian3}};

    public static final String[] WOMAN_STYLE_NAME = {
            "BlackonBlack", "Bohemian", "BusinessWoman", "CasualWoman", "Country", "Cute",
            "Elegant", "Exotic", "Formal", "Funky", "Girly", "Preppy",
            "Punk", "Rocker", "Sexy", "SmartCasual", "Sophisticated", "Sporty",
            "Street", "Tomboy", "Trendy", "Vibrant", "Vintage"
    };

    public static final int[] WOMAN_STYLE_DESCRIPTION = {
            R.string.wblackonblack,
            R.string.wbohemain,
            R.string.wbusinesswoman,
            R.string.wcasualwoman,
            R.string.wcountry,
            R.string.wcute,
            R.string.welegant,
            R.string.wexotic,
            R.string.wformal,
            R.string.wfunky,
            R.string.wgirly,
            R.string.wpreppy,
            R.string.wpunk,
            R.string.wrocker,
            R.string.wsexy,
            R.string.wsmartcasual,
            R.string.wsophisticated,
            R.string.wsporty,
            R.string.wstreet,
            R.string.wtomboy,
            R.string.wtrendy,
            R.string.wvibrant,
            R.string.wvintage
    };

    public static final int[][] WOMAN_STYLE_IMAGE = {
            {R.drawable.wblackonblack, R.drawable.wblackonblack2, R.drawable.wblackonblack3},
            {R.drawable.wbohemian, R.drawable.wbohemian2, R.drawable.wbohemian3},
            {R.drawable.wbusinesswoman, R.drawable.wbusinesswoman2, R.drawable.wbusinesswoman3},
            {R.drawable.wcasualwomen, R.drawable.wcasualwomen2, R.drawable.wcasualwomen3},
            {R.drawable.wcountry, R.drawable.wcountry2, R.drawable.wcountry3},
            {R.drawable.wcute, R.drawable.wcute2, R.drawable.wcute3, R.drawable.wcute4},
            {R.drawable.welegant, R.drawable.welegant2, R.drawable.welegant3, R.drawable.welegant4, R.drawable.welegant5},
            {R.drawable.wexotic, R.drawable.wexotic2, R.drawable.wexotic3},
            {R.drawable.wformal, R.drawable.wformal2, R.drawable.wformal3, R.drawable.wformal4, R.drawable.wformal5},
            {R.drawable.wfunky, R.drawable.wfunky2, R.drawable.wfunky3},
            {R.drawable.wgirly, R.drawable.wgirly2, R.drawable.wgirly3},
            {R.drawable.wpreppy, R.drawable.wpreppy2, R.drawable.wpreppy3},
            {R.drawable.wpunk, R.drawable.wpunk2, R.drawable.wpunk3},
            {R.drawable.wrocker, R.drawable.wrocker2, R.drawable.wrocker3},
            {R.drawable.wsexy, R.drawable.wsexy2, R.drawable.wsexy3, R.drawable.wsexy4},
            {R.drawable.wsmartcasual, R.drawable.wsmartcasual2, R.drawable.wsmartcasual3},
            {R.drawable.wsophisticated, R.drawable.wsophisticated2, R.drawable.wsophisticated3},
            {R.drawable.wsporty, R.drawable.wsporty2, R.drawable.wsporty3},
            {R.drawable.wstreet, R.drawable.wstreet2, R.drawable.wstreet3, R.drawable.wstreet4},
            {R.drawable.wtomboy, R.drawable.wtomboy2, R.drawable.wtomboy3, R.drawable.wtomboy4},
            {R.drawable.wtrendy, R.drawable.wtrendy2, R.drawable.wtrendy3},
            {R.drawable.wvibrant, R.drawable.wvibrant2, R.drawable.wvibrant3},
            {R.drawable.wvintage, R.drawable.wvintage2, R.drawable.wvintage3}};

    //Todo: PayPal client ID: This is for testing. Should be replaced with correct client id and secret key later.
    public static final String PAYPAL_CLIENT_ID = "AZLEARoSJdJhGwICsP6f_BAmpoVroI7kzyaTemXuMAB5o7RH1ui9FIHdoRftb96VbUIJzC50tcewF_TB";
    public static final String PAYPAL_SECRET = "EAgsW_UelSrmKo2Mr-dEF5ycBj03beswOvnxRGB7I9kkvL1IaPxahX1XDlFMTHYoBvrdRKDln4421d9I";
}
