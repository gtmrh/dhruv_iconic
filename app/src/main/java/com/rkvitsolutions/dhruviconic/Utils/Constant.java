package com.rkvitsolutions.dhruviconic.Utils;

public class Constant {

//    public static final String Base = "http://rmisapi.go4online.co.in";
    public static final String Base = "http://horticulturebihar.co.in/";
    public static final String Projects = "api/Stock/Project";
    public static final String PlotStock = "api/Stock/PlotStock?";
    public static final String Designation = "api/EMP/Designation";

//    public static final String Root = "http://rmisapi.go4online.co.in/api/Home/";
        public static final String Root = "http://horticulturebihar.co.in/api/Home/";
    public static final String Login = Root + "LogIn?";
    public static final String GetLeads = "api/Home/Leads?";
    public static final String GetCandidate = "api/Candidate/CandidateData?";
    public static final String AddFollowup = Root + "AddLeads?";
    public static final String AddLeadFollowup ="api/Home/AddLeads?";
    public static final String EmpList = "api/Attendance/employee";
    public static final String Url = "http://rem.go4online.co.in/index.aspx";

//    public static final String AddCandidateFollowup = "http://rmisapi.go4online.co.in/api/Candidate/AddCandidate?";
    public static final String AddCandidateFollowup = "http://horticulturebihar.co.in/api/Candidate/AddCandidate?";

    public static final String GetFollowup = "api/Folowup/FollowupLeads?";
    public static final String GetInterviews = "api/Candidate/CandidateFollowData?";
    public static final String GetLeadsSts = "http://horticulturebihar.co.in/api/Folowup/Leadstatus?id=";
    public static final String GetCandidateLeadsSts = "http://horticulturebihar.co.in/api/Candidate/CandidateLeadstatus?id=";
    public static final String GetTargetSts = "http://horticulturebihar.co.in/api/Target/TargetAcheived?UserID=";
    public static final String MarkAttend = "api/Attendance/Attendance?";
    public static final String MonthAtndSts = "api/Attendance/EmployeeStatus?";


public static final String Resign = "api/EMPStatus/EmployeeAppliedResign?";

    public static final String LEAVE_LIST = "api/EMPStatus/EmployeeLeaveStatus?";

    public static final String CallingReport = "api/Folowup/LeadstatusIndivisual?";

    public static final String MarkAttendUrl = "http://horticulturebihar.co.in/api/Attendance?";

    public static final String Leave = "/api/Attendance/EmployeeLeave?";

    public static final String AboutUs = "https://www.dhruviconic.com/about-us.html";
    public static final String ContactUs = "https://www.dhruviconic.com/contact-us.html";
    public static final String FollowUpDetails = "api/Attendance/FollowUpMobileNo?";
    public static final String SourceStsReport = "api/Attendance/EmployeeLoginInfo?";
    public static final String AllFollowUp = "api/Home/EMPFollowReport?";
    public static final String GetJoinings = "api/Employee/CandidateSelectedData?";

    public static String MailMsg = "Dear Candidate ,\n" +
            "\n" +
            "You are supposed to report on 00-00-000\n" +
            "\n" +
            "at 0:00 am for joining.\n" +
            "\n" +
            " \n" +
            "\n" +
            "Bring the following documents at the time of the joining :-\n" +
            "\n" +
            "1. 10th Marksheet\n" +
            "\n" +
            "2. 12th Marksheet\n" +
            "\n" +
            "3. Graduation Marksheet\n" +
            "\n" +
            "4. Master Marksheet\n" +
            "\n" +
            "5. Experience Certificate\n" +
            "\n" +
            "6. Aadhar Card\n" +
            "\n" +
            "7. Pan Card\n" +
            "\n" +
            "8. 5 passport size recent photo\n" +
            "\n" +
            "(Please bring photocopy of the above documents )\n" +
            "\n" +
            "Note : Revert your acceptance mail in this mail ID (i.e I got it and ready to work with your company)\n" +
            "\n" +
            "Please feel free to call/mail me for further clarifications.";

    public static final String AddBoysTarget = "api/Employee/AddEmployeeSetTarget?";
    public static final String GetBoysTarget = "api/Employee/EmployeeSetTargetList?";
    public static final String AddGirlsfTarget = "api/Employee/AddEmployeeGirlSetTarget?";
    public static final String GetGirlsTarget = "api/EMP/EmployeeSetTargetGirlsList?";


    public static final String SelfEvaluation = "api/SelfEvaluation/SelfEvaluationHdr?";
}
