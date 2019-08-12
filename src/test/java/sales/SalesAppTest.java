package sales;

import mockit.integration.junit4.JMockit;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.*;

@RunWith(JMockit.class)
public class SalesAppTest {

	@Test
	public void testCheckEffectiveFrom_givenEffectiveToAndEffectiveFrom_shouldReturnFalse(){

		//Given
		Sales sales = spy(new Sales());
		SalesApp salesApp = spy(new SalesApp());
		//When
		doReturn(new Date(System.currentTimeMillis()+86400)).when(sales).getEffectiveTo();
		doReturn(new Date(System.currentTimeMillis()-86400)).when(sales).getEffectiveFrom();
		//Then
		Assert.assertFalse(salesApp.checkEffectiveFrom(sales));
	}
	@Test
	public void testGetFilterReportData_givenSalesReportData_thenRunTimeOne(){

		Sales sales = spy(new Sales());
		SalesApp salesApp = spy(new SalesApp());
		SalesReportDao salesReportDao = spy(new SalesReportDao());
		SalesReportData salesReportDataFirst = spy(new SalesReportData());
		SalesReportData salesReportDataSecord = spy(new SalesReportData());
		List<SalesReportData> salesReportDataList = spy(new ArrayList<SalesReportData>());

		doReturn(Arrays.asList(salesReportDataFirst,salesReportDataSecord)).when(salesReportDao).getReportData(sales);
		doReturn("SalesActivity").when(salesReportDataFirst).getType();
		doReturn(true).when(salesReportDataFirst).isConfidential();
		doReturn("no").when(salesReportDataSecord).getType();
		salesApp.getFilterReportData(true,salesReportDao,salesReportDataList,sales);

		verify(salesReportDataList,times(1)).add(any());
	}

	@Test
	public void testGetSalesReportDataList_givenReportDataListAndMaxRow_thenReturnList(){
		SalesApp salesApp = spy(new SalesApp());
		List<SalesReportData> result = salesApp.getSalesReportDataList(1,Arrays.asList(new SalesReportData(),new SalesReportData()));
		Assert.assertEquals(2,result.size());
	}

	@Test
	public void testGenerateSalesActivityReport() {

		//Given
		SalesApp salesApp = spy(new SalesApp());
		Sales sales = spy(new Sales());
		new mockit.MockUp<SalesDao>() {
			@mockit.Mock
			public Sales getSalesBySalesId(String salesId) {
				return sales;
			}
		};
		new mockit.MockUp<SalesReportDao>() {
			@mockit.Mock
			public List<SalesReportData> getReportData(Sales sales) {
				return null;
			}
		};
		new mockit.MockUp<SalesApp>() {
			@mockit.Mock
			public List<SalesReportData> getFilterReportData(boolean isSupervisor, SalesReportDao salesReportDao, List<SalesReportData> filteredReportDataList, Sales sales) {
				return null;
			}
		};

		new mockit.MockUp<SalesApp>() {
			@mockit.Mock
			public List<SalesReportData> getTempList(int maxRow, List<SalesReportData> reportDataList) {
				return null;
			}
		};
		new mockit.MockUp<SalesApp>() {
			@mockit.Mock
			public SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
				return new SalesActivityReport();
			}
		};
		new mockit.MockUp<SalesActivityReport>() {
			@mockit.Mock
			public String toXml() {
				return "xml";
			}
		};
		doReturn(false).when(salesApp).checkEffectiveFrom(sales);

		//then
		try {
			salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);
		} catch (Exception e) {
			Assert.fail();
		}

	}

	@Test
	public void testGenerateReport() {
		Sales sales = spy(new Sales());
		SalesApp salesApp = spy(new SalesApp());
		new mockit.MockUp<SalesDao>() {
			@mockit.Mock
			public Sales getBySalesId(String salesId) {
				return sales;
			}
		};
		new mockit.MockUp<SalesReportDao>() {
			@mockit.Mock
			public List<SalesReportData> getSalesReportData(Sales sales) {
				return null;
			}
		};
		new mockit.MockUp<SalesApp>() {
			@mockit.Mock
			public List<SalesReportData> getTempList(int maxRow, List<SalesReportData> reportDataList) {
				return null;
			}
		};
		new mockit.MockUp<SalesApp>() {
			@mockit.Mock
			public SalesActivityReport getSalesActivityReport(List<String> headers, List<SalesReportData> reportDataList) {
				return new SalesActivityReport();
			}
		};
		new mockit.MockUp<SalesApp>() {
			@mockit.Mock
			public List<SalesReportData> getSalesFilterReportData(boolean isSupervisor, SalesReportDao salesReportDao, List<SalesReportData> filteredReportDataList, Sales sales) {
				return null;
			}
		};
		new mockit.MockUp<SalesActivityReport>() {
			@mockit.Mock
			public String toXml() {
				return "xml";
			}
		};
		doReturn(false).when(salesApp).checkEffectiveFrom(sales);
		salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);

		verify(salesApp,times(1)).generateReport(Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time"),null);
	}
}