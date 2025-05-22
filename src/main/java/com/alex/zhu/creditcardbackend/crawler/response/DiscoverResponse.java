package com.alex.zhu.creditcardbackend.crawler.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * POJO model of Discover’s cashback‐calendar JSON response.
 */
public class DiscoverResponse {

    /** The list of quarterly offers. */
    private List<Quarter> quarters;

    /** "Applies to all categories: " */
    private String disclosureApply;

    /** The common disclosure HTML. */
    private String disclosureCommon;

    // ─────────────── Getters & Setters ───────────────

    public List<Quarter> getQuarters() {
        return quarters;
    }

    public void setQuarters(List<Quarter> quarters) {
        this.quarters = quarters;
    }

    public String getDisclosureApply() {
        return disclosureApply;
    }

    public void setDisclosureApply(String disclosureApply) {
        this.disclosureApply = disclosureApply;
    }

    public String getDisclosureCommon() {
        return disclosureCommon;
    }

    public void setDisclosureCommon(String disclosureCommon) {
        this.disclosureCommon = disclosureCommon;
    }

    // ──────────────── Nested Quarter Class ────────────────

    /**
     * Represents one quarter’s cashback offer.
     */
    public static class Quarter {
        private long   offerId;
        private String offerStatus;
        private String title;

        @JsonProperty("offerDescUnenrolled")
        private String offerDescUnenrolled;

        @JsonProperty("offerDescEnrolled")
        private String offerDescEnrolled;

        private String imageBase;
        private String disclosureInd;
        private String quarterLabelStartDate;
        private String quarterLabelEndDate;
        private String preEnrollmentStartDate;
        private String qualificationStartDate;
        private String exclusionCriteria;
        private String inclusionCriteria;
        private boolean hasAdditionalCategories;
        private List<String> additionalCategories;
        private String disclosureUnenrolled;
        private String subTitle;
        private boolean defaultOffer;
        private String imageExt;
        private String imagePath;
        private String earnDescriptionDuringQualification;
        private String earnDescriptionPreEnrollment;
        private String middleSectionText;
        private String middleSectionButtonText;
        private String middleSectionButtonUrl;

        // ───── Getters & Setters for all fields ─────

        public long getOfferId() {
            return offerId;
        }
        public void setOfferId(long offerId) {
            this.offerId = offerId;
        }

        public String getOfferStatus() {
            return offerStatus;
        }
        public void setOfferStatus(String offerStatus) {
            this.offerStatus = offerStatus;
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public String getOfferDescUnenrolled() {
            return offerDescUnenrolled;
        }
        public void setOfferDescUnenrolled(String offerDescUnenrolled) {
            this.offerDescUnenrolled = offerDescUnenrolled;
        }

        public String getOfferDescEnrolled() {
            return offerDescEnrolled;
        }
        public void setOfferDescEnrolled(String offerDescEnrolled) {
            this.offerDescEnrolled = offerDescEnrolled;
        }

        public String getImageBase() {
            return imageBase;
        }
        public void setImageBase(String imageBase) {
            this.imageBase = imageBase;
        }

        public String getDisclosureInd() {
            return disclosureInd;
        }
        public void setDisclosureInd(String disclosureInd) {
            this.disclosureInd = disclosureInd;
        }

        public String getQuarterLabelStartDate() {
            return quarterLabelStartDate;
        }
        public void setQuarterLabelStartDate(String quarterLabelStartDate) {
            this.quarterLabelStartDate = quarterLabelStartDate;
        }

        public String getQuarterLabelEndDate() {
            return quarterLabelEndDate;
        }
        public void setQuarterLabelEndDate(String quarterLabelEndDate) {
            this.quarterLabelEndDate = quarterLabelEndDate;
        }

        public String getPreEnrollmentStartDate() {
            return preEnrollmentStartDate;
        }
        public void setPreEnrollmentStartDate(String preEnrollmentStartDate) {
            this.preEnrollmentStartDate = preEnrollmentStartDate;
        }

        public String getQualificationStartDate() {
            return qualificationStartDate;
        }
        public void setQualificationStartDate(String qualificationStartDate) {
            this.qualificationStartDate = qualificationStartDate;
        }

        public String getExclusionCriteria() {
            return exclusionCriteria;
        }
        public void setExclusionCriteria(String exclusionCriteria) {
            this.exclusionCriteria = exclusionCriteria;
        }

        public String getInclusionCriteria() {
            return inclusionCriteria;
        }
        public void setInclusionCriteria(String inclusionCriteria) {
            this.inclusionCriteria = inclusionCriteria;
        }

        public boolean isHasAdditionalCategories() {
            return hasAdditionalCategories;
        }
        public void setHasAdditionalCategories(boolean hasAdditionalCategories) {
            this.hasAdditionalCategories = hasAdditionalCategories;
        }

        public List<String> getAdditionalCategories() {
            return additionalCategories;
        }
        public void setAdditionalCategories(List<String> additionalCategories) {
            this.additionalCategories = additionalCategories;
        }

        public String getDisclosureUnenrolled() {
            return disclosureUnenrolled;
        }
        public void setDisclosureUnenrolled(String disclosureUnenrolled) {
            this.disclosureUnenrolled = disclosureUnenrolled;
        }

        public String getSubTitle() {
            return subTitle;
        }
        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public boolean isDefaultOffer() {
            return defaultOffer;
        }
        public void setDefaultOffer(boolean defaultOffer) {
            this.defaultOffer = defaultOffer;
        }

        public String getImageExt() {
            return imageExt;
        }
        public void setImageExt(String imageExt) {
            this.imageExt = imageExt;
        }

        public String getImagePath() {
            return imagePath;
        }
        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getEarnDescriptionDuringQualification() {
            return earnDescriptionDuringQualification;
        }
        public void setEarnDescriptionDuringQualification(String earnDescriptionDuringQualification) {
            this.earnDescriptionDuringQualification = earnDescriptionDuringQualification;
        }

        public String getEarnDescriptionPreEnrollment() {
            return earnDescriptionPreEnrollment;
        }
        public void setEarnDescriptionPreEnrollment(String earnDescriptionPreEnrollment) {
            this.earnDescriptionPreEnrollment = earnDescriptionPreEnrollment;
        }

        public String getMiddleSectionText() {
            return middleSectionText;
        }
        public void setMiddleSectionText(String middleSectionText) {
            this.middleSectionText = middleSectionText;
        }

        public String getMiddleSectionButtonText() {
            return middleSectionButtonText;
        }
        public void setMiddleSectionButtonText(String middleSectionButtonText) {
            this.middleSectionButtonText = middleSectionButtonText;
        }

        public String getMiddleSectionButtonUrl() {
            return middleSectionButtonUrl;
        }
        public void setMiddleSectionButtonUrl(String middleSectionButtonUrl) {
            this.middleSectionButtonUrl = middleSectionButtonUrl;
        }
    }
}
