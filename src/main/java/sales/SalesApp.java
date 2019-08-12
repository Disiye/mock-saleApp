package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

	public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {

		SalesDao salesDao = new SalesDao();
		SalesReportDao salesReportDao = new SalesReportDao();
		List<String> headers = null;
		List<SalesReportData> filteredReportDataList = new ArrayList<SalesReportData>();
		if (salesId == null) {
			return;
		}
		Sales sales = salesDao.getSalesBySalesId(salesId);
		if (checkEffectiveFrom(sales)) return;
		List<SalesReportData> reportDataList = getFilterReportData(isSupervisor, salesReportDao, filteredReportDataList, sales);
		getSalesReportDataList(maxRow, reportDataList);

		if (isNatTrade) {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		} else {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
		}
		SalesActivityReport report = this.generateReport(headers, reportDataList);
		uploadDocument(report);

	}

	public void uploadDocument(SalesActivityReport report) {
		EcmService ecmService = new EcmService();
		ecmService.uploadDocument(report.toXml());
	}

	public boolean checkEffectiveFrom(Sales sales) {
		Date today = new Date();
		return today.after(sales.getEffectiveTo())
				|| today.before(sales.getEffectiveFrom());
	}

	public List<SalesReportData> getFilterReportData(boolean isSupervisor, SalesReportDao salesReportDao, List<SalesReportData> filteredReportDataList, Sales sales) {
		List<SalesReportData> reportDataList = salesReportDao.getReportData(sales);

		for (SalesReportData data : reportDataList) {
			if ("SalesActivity".equalsIgnoreCase(data.getType())) {
				if (data.isConfidential()) {
					if (isSupervisor) {
						filteredReportDataList.add(data);
					}
				}else {
					filteredReportDataList.add(data);
				}
			}
		}
		return reportDataList;
	}

	public List<SalesReportData> getSalesReportDataList(int maxRow, List<SalesReportData> reportDataList) {
		List<SalesReportData> tempList = new ArrayList<SalesReportData>();
		for (int i=0; i < reportDataList.size() || i < maxRow; i++) {
			tempList.add(reportDataList.get(i));
		}
		return tempList;
	}

	public SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
		// TODO Auto-generated method stub
		return null;
	}

}