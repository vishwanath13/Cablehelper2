package com.vishwanathlokare.VendorHelper.models;

import java.util.Comparator;
import java.util.Date;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */








    /**
     * A dummy item representing a piece of content.
     */

        public String name;
        public final Long card;
        public final Long phone;
        public final boolean paid;
        public final int package_amo;
        public final String details;
        public final Date renewal;

    public Integer getPo() {
        return po;
    }

    public final Integer po;

    public String getLine() {
        return Line;
    }

    public final String Line;
        public Long getCard() {
            return card;
        }

        public Long getPhone() {
            return phone;
        }


        public boolean isPaid() {
            return paid;
        }

        public int getPackage_amo() {
            return package_amo;
        }

    public Date getRenewal() {
        return renewal;
    }

    public  String getName1() {
            return name;
        }

        public String getDetails() {
            return details;
        }

        public DummyContent(String name, Long card, Long phone, boolean paid, int package_amo,
                            String details, Date renewal, Integer po, String line) {
            this.card = card;
            this.renewal = renewal;
            this.phone = phone;
            this.name = name;
            this.paid = paid;
            this.package_amo = package_amo;
            this.details = details;
            this.po = po;
            Line = line;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Comparator<DummyContent> CompareDate = new Comparator<DummyContent>() {
            @Override
            public int compare(DummyContent dummyContent, DummyContent t1) {
                return dummyContent.getRenewal().compareTo(t1.getRenewal());
            }
        };

    public static Comparator<DummyContent> ComparePo = new Comparator<DummyContent>() {
        @Override
        public int compare(DummyContent dummyContent, DummyContent t1) {
            return dummyContent.getPo().compareTo(t1.getPo());
        }
    };



    }
