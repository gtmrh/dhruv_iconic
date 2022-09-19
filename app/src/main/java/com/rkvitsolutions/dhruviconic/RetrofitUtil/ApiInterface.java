package com.rkvitsolutions.dhruviconic.RetrofitUtil;

import com.rkvitsolutions.dhruviconic.Model.AllFollowupReportModel;
import com.rkvitsolutions.dhruviconic.Model.AtndModel;
import com.rkvitsolutions.dhruviconic.Model.AttendModel;
import com.rkvitsolutions.dhruviconic.Model.CallingStsModel;
import com.rkvitsolutions.dhruviconic.Model.CandidateModel;
import com.rkvitsolutions.dhruviconic.Model.DailyAtndReportModel;
import com.rkvitsolutions.dhruviconic.Model.DesignationModel;
import com.rkvitsolutions.dhruviconic.Model.EmpListModel;
import com.rkvitsolutions.dhruviconic.Model.FollowupDetailsModel;
import com.rkvitsolutions.dhruviconic.Model.FollowupModel;
import com.rkvitsolutions.dhruviconic.Model.JoiningsModel;
import com.rkvitsolutions.dhruviconic.Model.LeaveListModel;
import com.rkvitsolutions.dhruviconic.Model.LeaveModel;
import com.rkvitsolutions.dhruviconic.Model.LoginModel;
import com.rkvitsolutions.dhruviconic.Model.PlotModel;
import com.rkvitsolutions.dhruviconic.Model.ProjectModel;
import com.rkvitsolutions.dhruviconic.Model.SalesLeadsModel;
import com.rkvitsolutions.dhruviconic.Model.SourceStsModel;
import com.rkvitsolutions.dhruviconic.Model.TargetsModel;
import com.rkvitsolutions.dhruviconic.Utils.Constant;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET(Constant.Login)
    Call<LoginModel> login(@Query("UserID") String userid,
                           @Query("Password") String password);

    @GET(Constant.GetLeads)
    Call<List<SalesLeadsModel>> getLeads(@Query("ID") String ID);


    @GET(Constant.AddLeadFollowup)
    Call<String> addFollowup(@Query("Code") String ID,
                             @Query("LCode") String LCode,
                             @Query("Date") String Date,
                             @Query("DMode") String DMode,
                             @Query("Purpose") String Purpose,
                             @Query("Conversation") String Conversation,
                             @Query("FStatus") String FStatus,
                             @Query("FNDate") String FNDate,
                             @Query("NDate") String NDate,
                             @Query("CCode") String CCode,
                             @Query("PhoneNo") String PhoneNo,
                             @Query("Location") String Location,
                             @Query("Name") String Name,
                             @Query("Mode") String Mode,
                             @Query("Source") String Source,
                             @Query("Empcode") String Empcode,
                             @Query("CType") String CType,
                             @Query("MobileNo") String MobileNo,
                             @Query("DDProject") String DDProject);

    @GET(Constant.EmpList)
    Call<List<EmpListModel>> getEmpList();

    @GET(Constant.GetFollowup)
    Call<List<FollowupModel>> getFollowup(@Query("id") String ID,
                                          @Query("NDate") String NDate);

    @GET(Constant.CallingReport)
    Call<List<CallingStsModel>> getCallingReport(@Query("id") String ID,
                                                 @Query("Status") String Leads);

    @GET(Constant.MonthAtndSts)
    Call<List<DailyAtndReportModel>> getDailyAtndReport(@Query("EMPCode") String emp_id,
                                                        @Query("Dated") String Dated);

    @GET(Constant.LEAVE_LIST)
    Call<List<LeaveListModel>> getLeaveList(@Query("EMPCode") String emp_id);

    @GET(Constant.SourceStsReport)
    Call<List<SourceStsModel>> getSourceStsReport(@Query("EMPCode") String emp_id,
                                                  @Query("FDate") String FDate,
                                                  @Query("EDate") String EDate);

    @GET(Constant.AllFollowUp)
    Call<List<AllFollowupReportModel>> getAllFollowups(@Query("EMPID") String emp_id,
                                                       @Query("Rtype") String Rtype,
                                                       @Query("FDate") String FDate,
                                                       @Query("EDate") String EDate);

    @GET(Constant.AddBoysTarget)
    Call<String> addBoysTarget(@Query("ID") String id,
                               @Query("EMPCode") String emp_id,
                               @Query("Name") String dateSlot,
                               @Query("Location") String Location,
                               @Query("TotalData") String TotalData,
                               @Query("Meeting") String Meeting,
                               @Query("SiteVisit") String SiteVisit,
                               @Query("Login") String Login);

    @GET(Constant.AddGirlsfTarget)
    Call<String> addGirlsfTarget(@Query("ID") String id,
                                 @Query("EMPCode") String emp_id,
                                 @Query("Name") String dateSlot,
                                 @Query("Location") String Location,
                                 @Query("DialCall") String DialCall,
                                 @Query("AnswerCall") String AnswerCall,
                                 @Query("Meeting") String Meeting,
                                 @Query("SiteVisit") String SiteVisit,
                                 @Query("Login") String Login);

    @GET(Constant.GetBoysTarget)
    Call<List<TargetsModel>> getBoysTargets(@Query("id") String id);

    @GET(Constant.GetGirlsTarget)
    Call<List<TargetsModel>> getGirlsTargets(@Query("id") String id);

    @GET(Constant.GetJoinings)
    Call<List<JoiningsModel>> getJoinings(@Query("id") String id,
                                          @Query("FDate") String FDate,
                                          @Query("EDate") String EDate);

//    @GET(Constant.MarkAttend)
//    Call<AtndModel> markAttnd(@Query("EMPCode") String EMPCode,
//                              @Query("Location") String Location,
//                              @Query("Picks") String Picks);


    @POST(Constant.MarkAttend)
    @Multipart
    Call<AtndModel> markAttnd(@Part("EMPCode") String EMPCode,
                              @Part("Location") String Location,
                              @Part MultipartBody.Part image);

    @GET(Constant.Projects)
    Call<List<ProjectModel>> getProjects();

    @GET(Constant.Designation)
    Call<List<DesignationModel>> getDesignation();


    @GET(Constant.PlotStock)
    Call<List<PlotModel>> getPlots(@Query("PRCode") String PRCode);

    @GET(Constant.GetCandidate)
    Call<List<CandidateModel>> getCandidates(@Query("id") String id);

    @GET(Constant.GetInterviews)
    Call<List<CandidateModel>> getInterviews(@Query("id") String id,
                                             @Query("Status") String Status);

    @POST(Constant.MarkAttend)
    @Multipart
    Call<AttendModel> markAttend(@Part("EMPCode") String PRCode,
                                 @Part("Location") String Location,
                                 @Part MultipartBody.Part image);

    @GET(Constant.Leave)
    Call<LeaveModel> applyLeave(@Query("EMPCode") String EMPCode,
                                @Query("Dated") String Dated,
                                @Query("LType") String LeaveType,
                                @Query("FromDate") String FromDate,
                                @Query("ToDate") String ToDate,
                                @Query("Remarks") String Orders,
                                @Query("WorkAssign") String WorkAssign);

    @GET(Constant.Resign)
    Call<LeaveModel> applyResign(@Query("EMPCode") String EMPCode,
                                @Query("Dated") String Dated,
                                @Query("FromDate") String FromDate,
                                @Query("ToDate") String ToDate,
                                @Query("Remarks") String Remarks);

//    http://localhost:5769/api/SelfEvaluation/SelfEvaluationHdr?
//    // Dated=2022-02-01&ForMonth=2&ForYear=2022&EmpCode=0003&EmployeeRemarks=NONE&TrainingRequired=NONE&
//    // TLRemarks=None&HRRemarks=NONE&EmpType=SALES&One=1&Tow=2&Three=3&Four=4&Five=5&Six=6&Seven=7&Eight=8&Nine=9&Ten=10
//    // &Eleven=11&Twelve=12&Thirtin=13&Fourtion=14&Fifteen=15


    @GET(Constant.SelfEvaluation)
    Call<LeaveModel> applySelfEvl(@Query("EMPCode") String EMPCode,
                                 @Query("Dated") String Dated,
                                 @Query("ForMonth") String ForMonth,
                                  @Query("ForYear") String ForYear,
                                 @Query("EmployeeRemarks") String EmployeeRemarks,
                                 @Query("TrainingRequired") String TrainingRequired,
                                  @Query("TLRemarks") String TLRemarks,
                                  @Query("HRRemarks") String HRRemarks,
                                  @Query("EmpType") String EmpType,
                                  @Query("One") String One,
                                 @Query("Tow") String Tow,
                                  @Query("Three") String Three,
                                  @Query("Four") String Four,
                                  @Query("Five") String Five,
                                  @Query("Six") String Six,
                                  @Query("Seven") String Seven,
                                  @Query("Eight") String Eight,
                                  @Query("Nine") String Nine,
                                  @Query("Ten") String Ten,
                                  @Query("Eleven") String Eleven,
                                  @Query("Twelve") String Twelve,
                                  @Query("Thirtin") String Thirtin,
                                  @Query("Fourtion") String Fourtion,
                                  @Query("Fifteen") String Fifteen);

    @GET(Constant.FollowUpDetails)
    Call<List<FollowupDetailsModel>> followpupDetails(@Query("MobileNo") String MobileNo);

}