package com.yash.service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import com.yash.dao.JobsDAO;
import com.yash.entities.Jobs;
import com.yash.helper.FactoryEmployeeDB;
import com.yash.model.JobsModel;

public class JobsServiceImpl implements JobsService {
    static Logger log = Logger.getLogger(JobsServiceImpl.class.getName());

    private JobsDAO jobsDAO;
	public JobsServiceImpl() {
		this.jobsDAO=FactoryEmployeeDB.createJobsDAO();
        BasicConfigurator.configure();
	}
	@Override
	public List<JobsModel> retrieveJobs() {
		List<JobsModel> jobsModelList=new ArrayList<>();
		try {
			List<Jobs> jobsList=jobsDAO.getAllJobs();
			for(Jobs jobs:jobsList) {
				JobsModel jobsModel=new JobsModel();
				jobsModel.setJobId(jobs.getJobId());
				jobsModel.setJobTitle(jobs.getJobTitle());
				jobsModelList.add(jobsModel);
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.error("!ERROR[Retrieval of Jobs failed!!!]",e);
		}
		return jobsModelList;
	}
}
