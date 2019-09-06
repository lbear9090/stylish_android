package com.stylelist.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;
import com.stylelist.CustomViews.CustomFontButton;
import com.stylelist.CustomViews.CustomFontTextView;
import com.stylelist.R;

import java.util.ArrayList;
import java.util.Arrays;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

public class FilterActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_SEARCH_FILTER = 1005;

    private ArrayList<String> filterKeyArray = new ArrayList<>();
    private ArrayList<String> conditionKeyArray = new ArrayList<>();
    private ArrayList<String> locationKeyArray = new ArrayList<>();
    private ArrayList<String> saleTypeKeyArray = new ArrayList<>();

    private CustomFontButton btnBack;
    private FlexboxLayout categoryFilterView, conditionFilterView, locationFilterView, saleTypeFilterView;
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> countries = new ArrayList<>();
    private ArrayList<String> conditions = new ArrayList<>();
    private ArrayList<String> saleTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        btnBack = findViewById(R.id.backButton);
        categoryFilterView = findViewById(R.id.category_filter);
        conditionFilterView = findViewById(R.id.condition_filter);
        locationFilterView = findViewById(R.id.location_filter);
        saleTypeFilterView = findViewById(R.id.sale_type_filter);

        btnBack.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            filterKeyArray = bundle.getStringArrayList("filter_keys");
            conditionKeyArray = bundle.getStringArrayList("condition_keys");
            locationKeyArray = bundle.getStringArrayList("location_keys");
            saleTypeKeyArray = bundle.getStringArrayList("sale_type_keys");
        }

        String[] categoryStringArray = getResources().getStringArray(R.array.hash_tags);
        categories.addAll(Arrays.asList(categoryStringArray));
        String[] conditionStringArray = getResources().getStringArray(R.array.conditions);
        conditions.addAll(Arrays.asList(conditionStringArray));
        String[] countryStringArray = getResources().getStringArray(R.array.countries);
        countries.addAll(Arrays.asList(countryStringArray));
        String[] saleTypeStringArray = getResources().getStringArray(R.array.item_sale_type);
        saleTypes.addAll(Arrays.asList(saleTypeStringArray));

        ChipCloudConfig config = new ChipCloudConfig()
                .selectMode(ChipCloud.SelectMode.multi)
                .checkedChipColor(Color.parseColor("#D2A784"))
                .checkedTextColor(Color.parseColor("#ffffff"))
                .uncheckedChipColor(Color.parseColor("#e0e0e0"))
                .uncheckedTextColor(Color.parseColor("#000000"));

        ChipCloud categoryChipView = new ChipCloud(this, categoryFilterView, config);
        categoryChipView.addChips(categoryStringArray);
        categoryChipView.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int index, boolean checked, boolean userClick) {
                if(userClick) {
                    updateFilterKeys(index, checked);
                }
            }
        });

        ChipCloud conditionChipView = new ChipCloud(this, conditionFilterView, config);
        conditionChipView.addChips(conditionStringArray);
        conditionChipView.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int index, boolean checked, boolean userClick) {
                if(userClick) {
                    updateConditionsKeys(index, checked);
                }
            }
        });

        ChipCloud locationChipView = new ChipCloud(this, locationFilterView, config);
        locationChipView.addChips(countryStringArray);
        locationChipView.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int index, boolean checked, boolean userClick) {
                if(userClick) {
                    updateLocationKeys(index, checked);
                }
            }
        });

        ChipCloud saleTypeChipView = new ChipCloud(this, saleTypeFilterView, config);
        saleTypeChipView.addChips(saleTypeStringArray);
        saleTypeChipView.setListener(new ChipListener() {
            @Override
            public void chipCheckedChange(int index, boolean checked, boolean userClick) {
                if(userClick) {
                    updateSaleTypeKeys(index, checked);
                }
            }
        });

        for (String key : filterKeyArray) {
            int index = categories.indexOf(key);
            categoryChipView.setChecked(index);
        }
        for (String key : conditionKeyArray) {
            int index = conditions.indexOf(key);
            conditionChipView.setChecked(index);
        }
        for (String key : locationKeyArray) {
            int index = countries.indexOf(key);
            locationChipView.setChecked(index);
        }
        for (String key : saleTypeKeyArray) {
            int index = saleTypes.indexOf(key);
            saleTypeChipView.setChecked(index);
        }
    }

    private void updateFilterKeys(int index, boolean isChecked) {
        if (isChecked) {
            if (!filterKeyArray.contains(categories.get(index)))
                filterKeyArray.add(categories.get(index));
        } else {
            if (filterKeyArray.contains(categories.get(index)))
                filterKeyArray.remove(categories.get(index));
        }
        String keyText = "";
        for (String key : filterKeyArray) {
            keyText = keyText + " #" + key;
        }
    }

    private void updateConditionsKeys(int index, boolean isChecked) {
        if (isChecked) {
            if (!conditionKeyArray.contains(conditions.get(index)))
                conditionKeyArray.add(conditions.get(index));
        } else {
            if (conditionKeyArray.contains(conditions.get(index)))
                conditionKeyArray.remove(conditions.get(index));
        }
        String keyText = "";
        for (String key : conditionKeyArray) {
            keyText = keyText + " #" + key;
        }
    }

    private void updateLocationKeys(int index, boolean isChecked) {
        if (isChecked) {
            if (!locationKeyArray.contains(countries.get(index)))
                locationKeyArray.add(countries.get(index));
        } else {
            if (locationKeyArray.contains(countries.get(index)))
                locationKeyArray.remove(countries.get(index));
        }
        String keyText = "";
        for (String key : locationKeyArray) {
            keyText = keyText + " #" + key;
        }
    }

    private void updateSaleTypeKeys(int index, boolean isChecked) {
        if (isChecked) {
            if (!saleTypeKeyArray.contains(saleTypes.get(index)))
                saleTypeKeyArray.add(saleTypes.get(index));
        } else {
            if (saleTypeKeyArray.contains(saleTypes.get(index)))
                saleTypeKeyArray.remove(saleTypes.get(index));
        }
        String keyText = "";
        for (String key : saleTypeKeyArray) {
            keyText = keyText + " #" + key;
        }
    }

    private boolean checkValidation() {
        if (filterKeyArray.size() == 0 && conditionKeyArray.size() == 0 && locationKeyArray.size() == 0 && saleTypeKeyArray.size() == 0) {
            return true;
        } else {
            if (saleTypeKeyArray.size() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You haven't selected any 'Searching For' filters. Please select whether you're searching for 'Sale' 'Hire' or 'Inspiration' items")
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            if (checkValidation()) {
                Intent result = new Intent();
                result.putStringArrayListExtra("filter_keys", filterKeyArray);
                result.putStringArrayListExtra("condition_keys", conditionKeyArray);
                result.putStringArrayListExtra("location_keys", locationKeyArray);
                result.putStringArrayListExtra("sale_type_keys", saleTypeKeyArray);
                setResult(RESULT_OK, result);
                finish();
            }
        }
    }
}
