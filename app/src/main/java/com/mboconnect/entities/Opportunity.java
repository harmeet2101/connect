package com.mboconnect.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mboconnect.R;
import com.mboconnect.constants.AppConstants;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.constants.KeyConstants;
import com.mboconnect.constants.ServiceConstants;
import com.mboconnect.utils.Utils;
import com.tenpearls.android.utilities.CollectionUtility;
import com.tenpearls.android.utilities.JsonUtility;
import com.tenpearls.android.utilities.StringUtility;

import java.util.ArrayList;

/**
 * Created by ali.mehmood on 6/22/2015.
 */
public class Opportunity extends BaseEntity implements Parcelable {

    private String opportunityId;
    private long startDate;
    private long endDate;
    private long created;
    private String title;
    private String description;
    private String companyName;
    private String companyId;
    private String companyImgUrl;
    private String companyTitle;
    private String companyIndustry;
    private String companyPhone;
    private String companyMobile;
    private String companyEmail;
    private Boolean isCompanyActive;
    private Boolean isCompanyMboCertified;
    private Boolean isCompanyEmailVerified;
    private int minRate;
    private int maxRate;
    private String minType;
    private String maxType;
    private String status;
    private String skillsJson;
    private String addressJson;
    private String companyAddressJson;
    private String authorJson;
    private boolean isFavorite;
    private boolean isResponded;
    private boolean isAccepted;
    private Address address;
    private Address companyAddress;
    private Author author;
    private String duration;
    private boolean isCompanyInfoVisible;

    private ArrayList<String> skills;

    public Opportunity() {

    }

    @Override
    public void set(String jsonString) {

        try {

            JsonObject jsonObject = JsonUtility.parseToJsonObject(jsonString).getAsJsonObject();
            JsonObject companyObject = null;
            JsonObject rateObject = null;
            JsonObject minRateObject = null;
            JsonObject maxRateObject = null;

            if (jsonObject.has(KeyConstants.KEY_COMPANY)) {

                companyObject = jsonObject.getAsJsonObject(KeyConstants.KEY_COMPANY);
            }
            if (jsonObject.has(KeyConstants.KEY_RATE_RANGE)) {

                rateObject = jsonObject.getAsJsonObject(KeyConstants.KEY_RATE_RANGE);
            }

            if (rateObject != null && rateObject.has(KeyConstants.KEY_MIN)) {

                minRateObject = rateObject.getAsJsonObject(KeyConstants.KEY_MIN);
            }

            if (rateObject != null && rateObject.has(KeyConstants.KEY_MAX)) {

                maxRateObject = rateObject.getAsJsonObject(KeyConstants.KEY_MAX);
            }

            JsonObject locationObject = jsonObject.getAsJsonObject(KeyConstants.KEY_LOCATION);
            JsonArray skillArray = jsonObject.getAsJsonArray(KeyConstants.KEY_SKILLS);
            JsonObject maxObject = jsonObject.getAsJsonObject(KeyConstants.KEY_MAX);
            JsonObject minObject = jsonObject.getAsJsonObject(KeyConstants.KEY_MIN);
            JsonObject authorObject = jsonObject.getAsJsonObject("managed_by");

            opportunityId = JsonUtility.getString(jsonObject, KeyConstants.KEY_ID);
            startDate = JsonUtility.getLong(jsonObject, KeyConstants.KEY_START_DATE);
            endDate = JsonUtility.getLong(jsonObject, KeyConstants.KEY_END_DATE);
            created = JsonUtility.getLong(jsonObject, KeyConstants.KEY_PUBLISHED_DATE);
            title = JsonUtility.getString(jsonObject, KeyConstants.KEY_TITLE);
            description = JsonUtility.getString(jsonObject, KeyConstants.KEY_DESCRIPTION);

            if (minRateObject != null) {

                minRate = JsonUtility.getInt(minRateObject, KeyConstants.KEY_AMOUNT);
                minType = JsonUtility.getString(minRateObject, KeyConstants.KEY_TYPE);
            }

            if (maxRateObject != null) {

                maxRate = JsonUtility.getInt(maxRateObject, KeyConstants.KEY_AMOUNT);
                maxType = JsonUtility.getString(maxRateObject, KeyConstants.KEY_TYPE);
            }

            status = JsonUtility.getString(jsonObject, KeyConstants.KEY_STATUS);
            isFavorite = JsonUtility.getBoolean(jsonObject, KeyConstants.KEY_FAVORITE);
            isResponded = JsonUtility.getBoolean(jsonObject, KeyConstants.KEY_RESPONDED);
            isCompanyInfoVisible = JsonUtility.getBoolean(jsonObject, KeyConstants.KEY_COMPANY_INFO_VISIBLE);

            if (!isResponded) {

                checkIfRespondedIsNumeric(jsonObject);
            }

            isAccepted = JsonUtility.getBoolean(jsonObject, KeyConstants.KEY_ACCEPTED);

            if (locationObject != null) {

                addressJson = locationObject.toString();
            }

            if (companyObject != null) {

                companyName = JsonUtility.getString(companyObject, KeyConstants.KEY_NAME);
                companyId = JsonUtility.getString(companyObject, KeyConstants.KEY_ID);
                companyImgUrl = ServiceConstants.SERVICE_IMAGE + JsonUtility.getString(companyObject, KeyConstants.KEY_IMAGE_URL);
                companyImgUrl = companyImgUrl.replace("/v1", "");
                isCompanyActive = JsonUtility.getBoolean(companyObject, KeyConstants.KEY_ACTIVE);
                isCompanyMboCertified = JsonUtility.getBoolean(companyObject, KeyConstants.KEY_MBO_CERTIFIED);

                if (companyObject.has(KeyConstants.KEY_CONTACT)) {

                    // Contact Object
                    JsonObject companyContactObject = companyObject.getAsJsonObject(KeyConstants.KEY_CONTACT);

                    companyIndustry = JsonUtility.getString(companyContactObject, KeyConstants.KEY_INDUSTRY);
                    companyTitle = JsonUtility.getString(companyContactObject, KeyConstants.KEY_TITLE);

                    JsonArray companyPhoneArray = companyContactObject.getAsJsonArray(KeyConstants.KEY_PHONE);

                    if (companyPhoneArray != null && companyPhoneArray.size() > 0) {

                        JsonObject companyno1 = companyPhoneArray.get(0).getAsJsonObject();
                        companyPhone = JsonUtility.getString(companyno1, KeyConstants.KEY_NUMBER);
                        if (companyPhoneArray.size() == 2) {
                            JsonObject companyno2 = companyPhoneArray.get(1).getAsJsonObject();
                            companyMobile = JsonUtility.getString(companyno2, KeyConstants.KEY_NUMBER);

                        }
                    }

                    if (companyContactObject.has(KeyConstants.KEY_ADDRESS)) {

                        // Contact Address Object
                        JsonObject companyAddressObj = companyContactObject.getAsJsonObject(KeyConstants.KEY_ADDRESS);
                        companyAddressJson = companyAddressObj.toString();
                    }

                    if (companyContactObject.has(KeyConstants.KEY_EMAIL)) {

                        // Company Email
                        JsonObject companyEmailObject = companyContactObject.getAsJsonObject(KeyConstants.KEY_EMAIL);
                        isCompanyEmailVerified = JsonUtility.getBoolean(companyEmailObject, KeyConstants.KEY_VERIFIED);
                        companyEmail = JsonUtility.getString(companyEmailObject, KeyConstants.KEY_ADDRESS);
                    }
                }
            }
            // Skills
            if (skillArray != null && skillArray.size() != 0) {

                skillsJson = skillArray.toString();
                // skills = new ArrayList<String> ();
                // for (int i = 0; i < skillArray.size (); i++) {
                //
                // skills.add (skillArray.get (i).getAsString ());
                //
                // }
            }

            if (minObject != null) {

                minRate = JsonUtility.getInt(minObject, KeyConstants.KEY_AMOUNT);
                minType = JsonUtility.getString(minObject, KeyConstants.KEY_TYPE);

            }

            if (maxObject != null) {

                maxRate = JsonUtility.getInt(maxObject, KeyConstants.KEY_AMOUNT);
                maxType = JsonUtility.getString(maxObject, KeyConstants.KEY_TYPE);

            }

            if (authorObject != null) {

                authorJson = authorObject.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkIfRespondedIsNumeric(JsonObject jsonObject) {

        String number = JsonUtility.getString(jsonObject, KeyConstants.KEY_RESPONDED);

        if (Utils.isNumeric(number)) {
            isResponded = true;
        } else {

            isResponded = false;
        }
    }

    public String getBudget() {
        if (minRate == 0 && maxRate == 0) {
            return "Negotiable";
        } else {
            return String.format("$%s - $%s", Utils.formatCurrency(minRate), Utils.formatCurrency(maxRate));
        }
    }

    public String getTimestamp() {

        String timestamp = "";

        long days = Utils.daysAgo(created);

        if (days <= 0) {

            long[] hours = Utils.hoursAgo(created);

            if (hours[0] > 0) {

                timestamp = (hours[0] > 1) ? String.format("%d %s", hours[0], Utils.getString(R.string.hours_ago)) : String.format("%d %s", hours[0], Utils.getString(R.string.hour_ago));

            } else if (hours[1] > 0) {

                timestamp = (hours[1] > 1) ? String.format("%d %s", hours[1], Utils.getString(R.string.minutes_ago)) : String.format("%d %s", hours[1], Utils.getString(R.string.minute_ago));

            }
        } else {

            if (days >= 365) {

                float noOfyears = days / 365;
                String years = Utils.formatTimeDuration(noOfyears);

                timestamp = (years != "1") ? String.format("%s %s", years, Utils.getString(R.string.years_ago)) : String.format("%s %s", years, Utils.getString(R.string.year_ago));
            } else if (days >= 30) {

                float noOfmonths = days / 30;
                String months = Utils.formatTimeDuration(noOfmonths);

                timestamp = (months != "1") ? String.format("%s %s", months, Utils.getString(R.string.months_ago)) : String.format("%s %s", months, Utils.getString(R.string.month_ago));
            } else {

                timestamp = (days > 1) ? String.format("%d %s", days, Utils.getString(R.string.days_ago)) : String.format("%d %s", days, Utils.getString(R.string.day_ago));
            }
        }

        return timestamp;
    }

    public String getOpportunityId() {

        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {

        this.opportunityId = opportunityId;

    }

    public boolean getisResponded() {

        return isResponded;
    }

    public boolean getisAccepted() {

        return isAccepted;
    }

    public long getStartDate() {

        return startDate;
    }

    public long getEndDate() {

        return endDate;
    }

    public String getFormattedDateString() {

        return String.format("%s - %s", Utils.getFormattedDateFromMilis(getStartDate(), AppConstants.OPPORTUNITY_LIST_DATE_FORMAT), Utils.getFormattedDateFromMilis(getEndDate(), AppConstants.OPPORTUNITY_LIST_DATE_FORMAT));
    }

    public String getRateRange() {

        return String.format("%s - %s", getMinRate(), getMaxRate());
    }

    public long getCreated() {

        return created;
    }

    public String getTitle() {

        return title;
    }

    public String getCompanyName() {

        return companyName;
    }

    public int getMinRate() {

        return minRate;
    }

    public int getMaxRate() {

        return maxRate;
    }

    public String getMinType() {

        return minType;
    }

    public String getMaxType() {

        return maxType;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getStatus() {

        return status;
    }

    public EnumConstants.OpportunitiesStatus getStatusToDisplay() {

        long hourNumber = -1;
        hourNumber = Utils.getHoursDifference(created);

        if (isAccepted() && isResponded()) {

            return EnumConstants.OpportunitiesStatus.ACCEPTED;

        } else if (!isAccepted() && isResponded()) {

            return EnumConstants.OpportunitiesStatus.RESPONDED;

        } else if (hourNumber != -1 && hourNumber < 12) {

            return EnumConstants.OpportunitiesStatus.NEW;

        }

        return EnumConstants.OpportunitiesStatus.KEY_CLOSED;
    }

    public ArrayList<String> getSkills() {

        if (CollectionUtility.isEmptyOrNull(skills) && !StringUtility.isEmptyOrNull(getSkillsJson())) {

            JsonArray skillJsonArray = JsonUtility.parseToJsonArray(skillsJson);
            skills = new ArrayList<String>();

            for (int i = 0; i < skillJsonArray.size(); i++) {

                skills.add(skillJsonArray.get(i).getAsString());
            }
        }
        return skills;
    }

    public boolean isFavorite() {

        return isFavorite;
    }

    public boolean isResponded() {

        return isResponded;
    }

    public Address getAddress() {

        if (address != null) {

            return address;
        }

        address = new Address();
        JsonObject locationObject = JsonUtility.parseToJsonObject(addressJson);
        JsonObject locationAddressObj = locationObject.getAsJsonObject(KeyConstants.KEY_ADDRESS);
        address.setName(JsonUtility.getString(locationObject, KeyConstants.KEY_NAME));
        address.setCity(JsonUtility.getString(locationAddressObj, KeyConstants.KEY_CITY));
        address.setState(JsonUtility.getString(locationAddressObj, KeyConstants.KEY_STATE));
        address.setZip(JsonUtility.getString(locationAddressObj, KeyConstants.KEY_ZIP));
        address.setCity(JsonUtility.getString(locationAddressObj, KeyConstants.KEY_CITY));
        address.setStreetOne(JsonUtility.getString(locationAddressObj, KeyConstants.KEY_STREET1));
        address.setStreetTwo(JsonUtility.getString(locationAddressObj, KeyConstants.KEY_STREET2));

        // Local Coordinates
        JsonArray locationCoordinates = locationObject.getAsJsonArray(KeyConstants.KEY_COORDINATES);

        if (locationCoordinates.size() == 2) {

            address.setCoordinates(new LatLng(locationCoordinates.get(0).getAsFloat(), locationCoordinates.get(1).getAsFloat()));
        }

        return address;
    }

    public Author getAuthor() {

        if (author != null) {

            return author;
        }

        if (StringUtility.isEmptyOrNull(authorJson)) {

            return null;
        }

        author = new Author();

        JsonObject authorObject = JsonUtility.parseToJsonObject(authorJson);
        author.setName(JsonUtility.getString(authorObject, KeyConstants.KEY_NAME));

        // Author Contact
        JsonObject authorContactObject = authorObject.getAsJsonObject(KeyConstants.KEY_CONTACT);
        author.setDesignation(JsonUtility.getString(authorContactObject, KeyConstants.KEY_TITLE));
        author.setImageUrl(JsonUtility.getString(authorObject, KeyConstants.KEY_IMAGE_URL));

        JsonArray authorPhoneArray = JsonUtility.getJsonArray(authorContactObject, KeyConstants.KEY_PHONE);
        if (authorPhoneArray != null && authorPhoneArray.size() > 0) {

            for (int i = 0; i < authorPhoneArray.size(); i++) {

                author.getPhone().put(Utils.capitalizeFirstLetter(JsonUtility.getString(authorPhoneArray.get(i).getAsJsonObject(), KeyConstants.KEY_TYPE)), "" + JsonUtility.getString(authorPhoneArray.get(i).getAsJsonObject(), KeyConstants.KEY_NUMBER));
            }
        }

        author.setIndustry(JsonUtility.getString(authorContactObject, KeyConstants.KEY_INDUSTRY));
        author.setDesignation(JsonUtility.getString(authorContactObject, KeyConstants.KEY_TITLE));

        // Address
        JsonObject authorAddressObj = authorContactObject.getAsJsonObject(KeyConstants.KEY_ADDRESS);
        author.getAddress().setCity(JsonUtility.getString(authorAddressObj, KeyConstants.KEY_CITY));
        author.getAddress().setState(JsonUtility.getString(authorAddressObj, KeyConstants.KEY_STATE));
        author.getAddress().setZip(JsonUtility.getString(authorAddressObj, KeyConstants.KEY_ZIP));
        author.getAddress().setStreetOne(JsonUtility.getString(authorAddressObj, KeyConstants.KEY_STREET1));
        author.getAddress().setStreetTwo(JsonUtility.getString(authorAddressObj, KeyConstants.KEY_STREET2));

        // Author Email
        JsonObject authorEmailObject = authorContactObject.getAsJsonObject(KeyConstants.KEY_EMAIL);
        author.setIsVerified(JsonUtility.getBoolean(authorEmailObject, KeyConstants.KEY_VERIFIED));
        author.setEmail(JsonUtility.getString(authorEmailObject, KeyConstants.KEY_ADDRESS));

        // User Id
        JsonObject userObject = authorContactObject.getAsJsonObject(KeyConstants.KEY_USER);
        // author.setAuthorId(JsonUtility.getString(userObject,
        // KeyConstants.KEY_ID));

        return author;
    }

    public void setId(String id) {

        this.opportunityId = id;
    }

    public void setStartDate(long startDate) {

        this.startDate = startDate;
    }

    public void setEndDate(long endDate) {

        this.endDate = endDate;
    }

    public void setCreated(long created) {

        this.created = created;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }

    public String getCompanyId() {

        return companyId;
    }

    public void setCompanyId(String companyId) {

        this.companyId = companyId;
    }

    public String getCompanyImgUrl() {

        return companyImgUrl;
    }

    public void setCompanyImgUrl(String companyImgUrl) {

        this.companyImgUrl = companyImgUrl;
    }

    public String getCompanyTitle() {

        return companyTitle;
    }

    public void setCompanyTitle(String companyTitle) {

        this.companyTitle = companyTitle;
    }

    public String getCompanyIndustry() {

        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {

        this.companyIndustry = companyIndustry;
    }

    public String getCompanyPhone() {

        return companyPhone;
    }

    public String getCompanyMobile() {

        return companyMobile;
    }

    public void setCompanyPhone(String companyPhone) {

        this.companyPhone = companyPhone;

    }

    public void setCompanyMobile(String companyMobile) {

        this.companyMobile = companyMobile;

    }

    public String getCompanyEmail() {

        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {

        this.companyEmail = companyEmail;
    }

    public Boolean getIsCompanyActive() {

        if (isCompanyActive == null) {

            return false;
        }
        return isCompanyActive;
    }

    public void setIsCompanyActive(Boolean isCompanyActive) {

        this.isCompanyActive = isCompanyActive;
    }

    public Boolean getIsCompanyMboCertified() {

        if (isCompanyMboCertified == null) {

            return false;
        }
        return isCompanyMboCertified;
    }

    public void setIsCompanyMboCertified(Boolean isCompanyMboCertified) {

        this.isCompanyMboCertified = isCompanyMboCertified;
    }

    public Boolean getIsCompanyEmailVerified() {

        if (isCompanyEmailVerified == null) {

            return false;
        }

        return isCompanyEmailVerified;
    }

    public void setIsCompanyEmailVerified(Boolean isCompanyEmailVerified) {

        this.isCompanyEmailVerified = isCompanyEmailVerified;
    }

    public void setMinRate(int minRate) {

        this.minRate = minRate;
    }

    public void setMaxRate(int maxRate) {

        this.maxRate = maxRate;
    }

    public void setMinType(String minType) {

        this.minType = minType;
    }

    public void setMaxType(String maxType) {

        this.maxType = maxType;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public void setSkills(ArrayList<String> skills) {

        this.skills = skills;
    }

    public void setIsFavorite(boolean isFavorite) {

        this.isFavorite = isFavorite;
    }

    public void setIsResponded(boolean isResponded) {

        this.isResponded = isResponded;
    }

    public boolean isAccepted() {

        return isAccepted;
    }

    public void setIsAccepted(boolean isAccepted) {

        this.isAccepted = isAccepted;
    }

    public void setAddress(Address address) {

        this.address = address;
    }

    public Address getCompanyAddress() {

        if (companyAddress != null) {

            return companyAddress;
        }

        JsonObject companyAddressObj = JsonUtility.parseToJsonObject(companyAddressJson);

        companyAddress = new Address();
        companyAddress.setCity(JsonUtility.getString(companyAddressObj, KeyConstants.KEY_CITY));
        companyAddress.setState(JsonUtility.getString(companyAddressObj, KeyConstants.KEY_STATE));
        companyAddress.setZip(JsonUtility.getString(companyAddressObj, KeyConstants.KEY_ZIP));
        companyAddress.setCity(JsonUtility.getString(companyAddressObj, KeyConstants.KEY_CITY));
        companyAddress.setStreetOne(JsonUtility.getString(companyAddressObj, KeyConstants.KEY_STREET1));

        return companyAddress;
    }

    public void setCompanyAddress(Address companyAddress) {

        this.companyAddress = companyAddress;
    }

    public void setAuthor(Author author) {

        this.author = author;
    }

    public void setDuration(String duration) {

        this.duration = duration;
    }

    public String getDuration() {

        return duration;
    }

    public Opportunity(Parcel in) {

        opportunityId = in.readString();
        startDate = in.readLong();
        endDate = in.readLong();
        created = in.readLong();
        title = in.readString();

        description = in.readString();
        companyName = in.readString();
        companyId = in.readString();
        companyImgUrl = in.readString();
        companyTitle = in.readString();
        companyIndustry = in.readString();
        companyPhone = in.readString();
        companyEmail = in.readString();
        byte isCompanyActiveVal = in.readByte();
        isCompanyActive = isCompanyActiveVal == 0x02 ? null : isCompanyActiveVal != 0x00;
        byte isCompanyMboCertifiedVal = in.readByte();
        isCompanyMboCertified = isCompanyMboCertifiedVal == 0x02 ? null : isCompanyMboCertifiedVal != 0x00;
        byte isCompanyEmailVerifiedVal = in.readByte();
        isCompanyEmailVerified = isCompanyEmailVerifiedVal == 0x02 ? null : isCompanyEmailVerifiedVal != 0x00;
        minRate = in.readInt();
        maxRate = in.readInt();
        minType = in.readString();
        maxType = in.readString();
        status = in.readString();
        if (in.readByte() == 0x01) {
            skills = new ArrayList<String>();
            in.readList(skills, String.class.getClassLoader());
        } else {
            skills = null;
        }
        isFavorite = in.readByte() != 0x00;
        isResponded = in.readByte() != 0x00;
        isAccepted = in.readByte() != 0x00;
        address = (Address) in.readValue(Address.class.getClassLoader());
        companyAddress = (Address) in.readValue(Address.class.getClassLoader());
        author = (Author) in.readValue(Author.class.getClassLoader());
        duration = in.readString();
        companyMobile = in.readString();
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(opportunityId);
        dest.writeLong(startDate);
        dest.writeLong(endDate);
        dest.writeLong(created);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(companyName);
        dest.writeString(companyId);
        dest.writeString(companyImgUrl);
        dest.writeString(companyTitle);
        dest.writeString(companyIndustry);
        dest.writeString(companyPhone);
        dest.writeString(companyEmail);
        if (isCompanyActive == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isCompanyActive ? 0x01 : 0x00));
        }
        if (isCompanyMboCertified == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isCompanyMboCertified ? 0x01 : 0x00));
        }
        if (isCompanyEmailVerified == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isCompanyEmailVerified ? 0x01 : 0x00));
        }
        dest.writeInt(minRate);
        dest.writeInt(maxRate);
        dest.writeString(minType);
        dest.writeString(maxType);
        dest.writeString(status);
        if (skills == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(skills);
        }
        dest.writeByte((byte) (isFavorite ? 0x01 : 0x00));
        dest.writeByte((byte) (isResponded ? 0x01 : 0x00));
        dest.writeByte((byte) (isAccepted ? 0x01 : 0x00));
        dest.writeValue(address);
        dest.writeValue(companyAddress);
        dest.writeValue(author);
        dest.writeString(duration);
        dest.writeString(companyMobile);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Opportunity> CREATOR = new Parcelable.Creator<Opportunity>() {
        @Override
        public Opportunity createFromParcel(Parcel in) {

            return new Opportunity(in);
        }

        @Override
        public Opportunity[] newArray(int size) {

            return new Opportunity[size];
        }
    };

    @Override
    public boolean equals(Object o) {

        if (o instanceof Opportunity) {

            return this.getOpportunityId().equals(((Opportunity) o).getOpportunityId());
        }
        return false;
    }

    public String getSkillsJson() {

        return skillsJson;
    }

    public void setSkillsJson(String skillsJson) {

        this.skillsJson = skillsJson;
    }

    public String getAddressJson() {

        return addressJson;
    }

    public String getCompanyAddressJson() {

        return companyAddressJson;
    }

    public String getAuthorJson() {

        return authorJson;
    }

    public boolean isCompanyInfoVisible() {

        return isCompanyInfoVisible;
    }

    public void setCompanyInfoVisible(boolean isCompanyInfoVisible) {

        this.isCompanyInfoVisible = isCompanyInfoVisible;
    }

    public void setAddressJson(String addressJson) {

        this.addressJson = addressJson;
    }

    public void setCompanyAddressJson(String companyAddressJson) {

        this.companyAddressJson = companyAddressJson;
    }

    public void setAuthorJson(String authorJson) {

        this.authorJson = authorJson;
    }
}