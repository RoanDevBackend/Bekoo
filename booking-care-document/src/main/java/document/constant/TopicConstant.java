package document.constant;

public class TopicConstant {
    public static class DepartmentTopic{
        public final static String CREATE_DEPARTMENT= "create-department-event";
        public final static String UPDATE_DEPARTMENT= "update-info-department-event";
        public final static String DELETE_DEPARTMENT= "delete-department-event";
    }

    public static class DoctorDepartmentTopic{
        /**
         * Thêm một bác sĩ vào khoa.
         */
        public final static String ADD_NEW_ONE="add-new-one-doctor-department-event";
        /**
         * Xoá một bác sĩ ra khỏi khoa.
         */
        public final static String DELETE="delete-doctor-department-event";
    }

    public static class DoctorTopic{
        public final static String CREATE="create-doctor-event";
        public final static String UPDATE_INFO ="update-info-doctor-event";
        /**
         * Đặt số người tối đa một ngày
         */
        public final static String SET_PERSON_PER_DAY= "set-maximum-people-per-day-event";
        public final static String DELETE= "delete-doctor-event";
    }

    public static class PatientTopic{
        public final static String CREATE_PATIENT="create-info-patient";
        /**
         * Thêm liên hệ khẩn cấp cho bệnh nhân
         */
        public final static String CREATE_CONTACT="create-info-contact";
        /**
         * Thêm tiền sử bệnh án cho bệnh nhân
         */
        public final static String CREATE_MEDICAL="create-medical-history";
        public final static String DELETE_CONTACT="delete-emergency-contact";
        /**
         * Xoá tiền sử bệnh án của bệnh nhân
         */
        public final static String DELETE_MEDICAL="delete-medical-history";
    }

    public static class ScheduleTopic{
        public final static String CREATE_SCHEDULE="create-schedule";
        public final static String DELETE_SCHEDULE="delete-schedule";
    }

    public static class SpecializeTopic{
        public final static String CREATE_SPECIALIZE="create-specialize";
        public final static String UPDATE_SPECIALIZE="update-specialize";
        public final static String DELETE_SPECIALIZE="delete-specialize";
    }
    public static class UserTopic{
        public final static String CREATE_USER="create-user-event";
        public final static String UPDATE_INFO_USER="update-info-user-event";
        public final static String UPDATE_AVATAR_USER="update-avatar-user-event";
        public final static String DELETE_USER="delete-user-event";
    }
}
