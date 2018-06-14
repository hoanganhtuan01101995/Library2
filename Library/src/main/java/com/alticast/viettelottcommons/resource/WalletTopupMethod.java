package com.alticast.viettelottcommons.resource;

import android.content.Context;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.util.WindDataConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class WalletTopupMethod {
    public static final String METHOD_PHONE = "phone";
    public static final String METHOD_SCARD = "scard";

    private String method = null;
    private Config config = null;
    private ArrayList<Integer> predefined_amount = null;
    private Promotion promotion = null;


    public String getMethod() {
        return method;
    }


    public Config getConfig() {
        return config;
    }

    public boolean checkHasPromotion() {
        if(promotion == null) return false;
        if(promotion.getItems() == null) return false;

        for(Promotion.Item item: promotion.getItems()) {
            if(item.getBonus_amount() > 0 || item.getBonus_rate() > 0) {
                if(item.getEnd_amont() < getMinValue() || item.getStart_amount() > getMaxValue()) {
                    continue;
                }
                return true;
            }
        }

        return false;
    }

    public int getMinValue() {
        if(predefined_amount == null || predefined_amount.size() == 0) return 0;

        return predefined_amount.get(0);
    }
    public int getMaxValue() {
        if(predefined_amount == null || predefined_amount.size() == 0) return 0;

        return predefined_amount.get(predefined_amount.size() - 1);
    }


    public ArrayList<Integer> getPredefined_amount() {
        return predefined_amount;
    }

    public void processData() {
        if(predefined_amount == null) return;

        Collections.sort(predefined_amount);

        if(config == null) return;
        if(config.getMaximum_amount() == 0 || config.getMinimum_amount() >= config.getMaximum_amount()) return;

        ArrayList<Integer> listFilter = null;

        for(Integer integer : predefined_amount) {
            if(integer >= config.getMinimum_amount() && integer <= config.getMaximum_amount()) {
                if(listFilter == null) {
                    listFilter = new ArrayList<>();
                }

                listFilter.add(integer);
            }
        }

        predefined_amount = listFilter;


    }

    public void setPredefined_amount(ArrayList<Integer> predefined_amount) {
        this.predefined_amount = predefined_amount;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getPromotionId() {
        return promotion != null ? promotion.getId() : null;
    }


    public class Config {
        private int minimum_amount = 0;
        private int maximum_amount = 0;

        public int getMinimum_amount() {
            return minimum_amount;
        }

        public void setMinimum_amount(int minimum_amount) {
            this.minimum_amount = minimum_amount;
        }

        public int getMaximum_amount() {
            return maximum_amount;
        }

        public void setMaximum_amount(int maximum_amount) {
            this.maximum_amount = maximum_amount;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "minimum_amount=" + minimum_amount +
                    ", maximum_amount=" + maximum_amount +
                    '}';
        }
    }

    public class Promotion {
        private String id = null;
        private String license_start = null;
        private String license_end = null;
        private MultiLingualText[] name = null;
        private MultiLingualText[] description = null;
        private MultiLingualText[] full_description = null;
        private Item[] items = null;


        public String getId() {
            return id;
        }

        public String getLicense_start() {
            return license_start;
        }

        public String getLicense_end() {
            return license_end;
        }

        public Item[] getItems() {
            return items;
        }

        public Item getItem(int predefined_amount) {
            if (items == null) {
                return null;
            }

            int size = items.length;
            Item preItem = null;
            for (int i = 0; i < size; i++) {
                int start_amount = items[i].start_amount;
                if (start_amount == predefined_amount) {
                    preItem = items[i];
                    break;
                }
            }
            return preItem;
        }

        public String getStartDate() {
            long time = TimeUtil.getLongTime(license_start, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
            return TextUtils.getDateString(time, WindDataConverter.FORMAT_DATE_PURCHASE);
        }
        public String getEndDate() {
            long time = TimeUtil.getLongTime(license_end, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
            return TextUtils.getDateString(time, WindDataConverter.FORMAT_DATE_PURCHASE);
        }

        public String getName(String lang) {
            if (name == null) {
                return null;
            }
            String value = null;
            int size = name.length;
            for (int i = 0; i < size; i++) {
                if (lang.equals(name[i].getLang())) {
                    value = name[i].getText();
                    break;
                }
            }
            return value;
        }

        public String getDescription(String lang) {
            if (description == null) {
                return null;
            }
            String value = null;
            int size = description.length;
            for (int i = 0; i < size; i++) {
                if (lang.equals(description[i].getLang())) {
                    value = description[i].getText();
                    break;
                }
            }
            return value;
        }

        public String getFull_description(String lang) {
            if (full_description == null) {
                return null;
            }
            String value = null;
            int size = full_description.length;
            for (int i = 0; i < size; i++) {
                if (lang.equals(full_description[i].getLang())) {
                    value = full_description[i].getText();
                    break;
                }
            }
            return value;
        }

        public String getDescription(Context context) {
            String des = null;
            if(full_description != null) {
                des = getFull_description(WindmillConfiguration.LANGUAGE_VIE);
                if(des == null) {
                    des = getFull_description(WindmillConfiguration.LANGUAGE_ENG);
                }
            }

            if(des == null) {
                if(description != null) {
                    des = getDescription(WindmillConfiguration.LANGUAGE_VIE);
                    if(des == null) {
                        des = getDescription(WindmillConfiguration.LANGUAGE_ENG);
                    }
                }
            }

            if(des == null) {
                des = context.getString(R.string.promotionTime);
            }

            return des;

        }

        public class Item {
            private int start_amount = 0;
            private int end_amount = 0;
            private int bonus_rate = 0;
            private int bonus_amount = 0;
            private boolean input = false;

            public int getStart_amount() {
                return start_amount;
            }

            public int getEnd_amont() {
                return end_amount;
            }

            public int getBonus_rate() {
                return bonus_rate;
            }

            public int getBonus_amount() {
                return bonus_amount;
            }

            public boolean isInput() {
                return input;
            }
        }
    }

    @Override
    public String toString() {
        return "WalletTopupMethod{" +
                "method='" + method + '\'' +
                ", config=" + config +
                ", predefined_amount=" + predefined_amount +
                '}';
    }
}
