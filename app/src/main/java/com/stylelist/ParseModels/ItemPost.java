package com.stylelist.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.stylelist.Utils.Constants;

/**
 * Created by Security on 3/22/2018.
 */
@ParseClassName("ItemPost")
public class ItemPost extends ParseObject {

    ParseFile filteredImage;

    public ItemPost() {
    }

    public float getXPosition() {
        try {
            return Float.valueOf(fetchIfNeeded().getString(Constants.ITEM_POST_X_POSITION));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public float getViewXPosition() {
        try {
            return Float.valueOf(fetchIfNeeded().getString(Constants.ITEM_POST_VIEW_X_POSITION));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setXPosition(float xPosition) {
        put(Constants.ITEM_POST_X_POSITION, xPosition);
    }

    public float getYPosition() {
        try {
            return Float.valueOf(fetchIfNeeded().getString(Constants.ITEM_POST_Y_POSITION));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public float getViewYPosition() {
        try {
            return Float.valueOf(fetchIfNeeded().getString(Constants.ITEM_POST_VIEW_Y_POSITION));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setYPosition(float yPosition) {
        put(Constants.ITEM_POST_Y_POSITION, yPosition);
    }

    public float getRectWidth() {
        try {
            return Float.valueOf(fetchIfNeeded().getString(Constants.ITEM_POST_RECT_WIDTH));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setRectWidth(float rectWidth) {
        put(Constants.ITEM_POST_RECT_WIDTH, rectWidth);
    }

    public float getRectHeight() {
        try {
            return Float.valueOf(fetchIfNeeded().getString(Constants.ITEM_POST_RECT_HEIGHT));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setRectHeight(float rectHeight) {
        put(Constants.ITEM_POST_RECT_HEIGHT, rectHeight);
    }

    public String getPostId() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_POST_ID);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setPostId(String postId) {
        put(Constants.ITEM_POST_POST_ID, postId);
    }

    public String getHashTag() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_HASH_TAG);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setHashTag(String hashTag) {
        put(Constants.ITEM_POST_HASH_TAG, hashTag);
    }

    public String getSaleType() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_SALE_TYPE);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setSaleType(String saleType) {
        put(Constants.ITEM_POST_SALE_TYPE, saleType);
    }

    public String getConditionDetail() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_CONDITION_DETAIL);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setConditionDetail(String conditionDetail) {
        put(Constants.ITEM_POST_CONDITION_DETAIL, conditionDetail);
    }

    public String getHashTagSize() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_HASH_TAG_SIZE);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setHashTagSize(String hashTagSize) {
        put(Constants.ITEM_POST_HASH_TAG_SIZE, hashTagSize);
    }

    public String getLocationCityInfo() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_LOCATION_CITY_INFO);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setLocationCityInfo(String locationInfo) {
        put(Constants.ITEM_POST_LOCATION_CITY_INFO, locationInfo);
    }

    public String getLocationCountryInfo() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_LOCATION_COUNTRY_INFO);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setLocationCountryInfo(String locationInfo) {
        put(Constants.ITEM_POST_LOCATION_COUNTRY_INFO, locationInfo);
    }

    public String getHashTagDescription() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_HASH_TAG_DESCRIPTION);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setHashTagDescription(String hashTagDescription) {
        put(Constants.ITEM_POST_HASH_TAG_DESCRIPTION, hashTagDescription);
    }

    public String getDelivery() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_DELIVERY);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setDelivery(String delivery) {
        put(Constants.ITEM_POST_DELIVERY, delivery);
    }

    public String getItemPrice() {
        try {
            String price = fetchIfNeeded().getString(Constants.ITEM_POST_ITEM_PRICE);
            if(price == null || price.equalsIgnoreCase(""))
            {
                return "0";
            }
            return price;
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
    }

    public void setItemPrice(String itemPrice) {
        put(Constants.ITEM_POST_ITEM_PRICE, itemPrice);
    }

    public String getDepositPrice() {
        try {
            String price = fetchIfNeeded().getString(Constants.ITEM_POST_DEPOSIT_PRICE);
            if(price == null || price.equalsIgnoreCase(""))
            {
                return "0";
            }
            return price;
        } catch (ParseException e) {
            e.printStackTrace();
            return "0";
        }
    }

    public void setDepositPrice(String itemPrice) {
        put(Constants.ITEM_POST_DEPOSIT_PRICE, itemPrice);
    }

    public String getShippingValue() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_DELIVERYSHIPPING);
        } catch (ParseException e) {
            e.printStackTrace();
            return "No";
        }
    }

    public String getMeetValue() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_DELIVERYPERSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return "No";
        }
    }
    public String getHashTagName() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_HASH_TAG_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setHashTagName(String hashTagName) {
        put(Constants.ITEM_POST_HASH_TAG_NAME, hashTagName);
    }

    public String getIntShipCost(){
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_INTERNATIONAL_COST);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getDomShipCost(){
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_DOMESTIC_COST);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    public String getCondition() {
        try {
            return fetchIfNeeded().getString(Constants.ITEM_POST_CONDITION);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setCondition(String condition) {
        put(Constants.ITEM_POST_CONDITION, condition);
    }

    public ParseFile getHashTagImageOne() {
        try {
            return fetchIfNeeded().getParseFile(Constants.ITEM_POST_IMAGE_ONE);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setHashTagImageOne(ParseFile hashTagImageOne) {
        put(Constants.ITEM_POST_IMAGE_ONE, hashTagImageOne);
    }

    public ParseFile getHashTagImageTwo() {
        try {
            return fetchIfNeeded().getParseFile(Constants.ITEM_POST_IMAGE_TWO);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setHashTagImageTwo(ParseFile hashTagImageTwo) {
        put(Constants.ITEM_POST_IMAGE_TWO, hashTagImageTwo);
    }

    public ParseFile getHashTagImageThree() {
        try {
            return fetchIfNeeded().getParseFile(Constants.ITEM_POST_IMAGE_THREE);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setHashTagImageThree(ParseFile hashTagImageThree) {
        put(Constants.ITEM_POST_IMAGE_THREE, hashTagImageThree);
    }

    public ParseFile getHashTagImageFour() {
        try {
            return fetchIfNeeded().getParseFile(Constants.ITEM_POST_IMAGE_FOUR);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setHashTagImageFour(ParseFile hashTagImageFour) {
        put(Constants.ITEM_POST_IMAGE_FOUR, hashTagImageFour);
    }

    public ParseFile getHashTagImageFive() {
        try {
            return fetchIfNeeded().getParseFile(Constants.ITEM_POST_IMAGE_FIVE);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setHashTagImageFive(ParseFile hashTagImageFive) {
        put(Constants.ITEM_POST_IMAGE_FIVE, hashTagImageFive);
    }

    public ParseFile getImage() {
        try {
            filteredImage = fetchIfNeeded().getParseFile(Constants.ITEM_POST_FILTERED_IMAGE);
        } catch (ParseException e) {
            e.printStackTrace();
            filteredImage = null;
        }

        return filteredImage;
    }


    public void setImage(ParseFile file) {
        put(Constants.ITEM_POST_FILTERED_IMAGE, file);
    }

}
