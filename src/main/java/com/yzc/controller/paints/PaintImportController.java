package com.yzc.controller.paints;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.MessageConvertUtil;
import com.yzc.utils.ObjectUtils;
import com.yzc.vos.ListViewModel;
import com.yzc.vos.ResTechInfoViewModel;
import com.yzc.vos.TechnologyRequirementViewModel;
import com.yzc.vos.paints.AuthorViewModel;
import com.yzc.vos.paints.PaintViewModel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@RestController
@RequestMapping("/paints/data")
public class PaintImportController {

	private final static Logger LOG = LoggerFactory.getLogger(PaintImportController.class);

	/**
	 * 导入名画作者信息
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/import/author")
	public Map<String, String> importPaintAuthor(HttpServletRequest request) {
		CommonsMultipartResolver resolve = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (resolve.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multiRequest.getFile("excel");

			try {
				BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
				WorkbookSettings setting = new WorkbookSettings();
				setting.setEncoding("ISO-8859-1");
				Workbook workbook = Workbook.getWorkbook(bis, setting);
				Sheet sheet = workbook.getSheet(0);
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				for (int i = 1; i < sheet.getRows(); i++) {
					Map<String, String> rowMap = new HashMap<String, String>();
					String id = "";
					String artist = "";
					String title = "";
					String continent = "";
					String nation = "";
					String objectType = "";
					String date = "";
					String medium = "";
					String dimensions = "";
					String currentLocation = "";
					for (int j = 0; j < 10; j++) {
						id = sheet.getCell(j++, i).getContents();
						artist = sheet.getCell(j++, i).getContents();
						title = sheet.getCell(j++, i).getContents();
						continent = sheet.getCell(j++, i).getContents();
						nation = sheet.getCell(j++, i).getContents();
						objectType = sheet.getCell(j++, i).getContents();
						date = sheet.getCell(j++, i).getContents();
						medium = sheet.getCell(j++, i).getContents();
						dimensions = sheet.getCell(j++, i).getContents();
						currentLocation = sheet.getCell(j++, i).getContents();
					}
					if (artist == null || artist == "") {
						artist = "unkonwn";
					}
					if (nation == null || nation == "") {
						nation = "unkonwn";
					}
					String authorId = null;
					AuthorViewModel avm = new AuthorViewModel();
					avm.setNationality(nation);
					if (artist.indexOf("(") != -1) {
						// 用（、-、）进行字符串分割，分别获取artist的name、birthday、deathdate
						String[] content = artist.split("-|\\(|\\)");
						avm.setAuthorName(content[0].trim());
						avm.setBirthdate(content[1].trim());
						avm.setDeathdate(content[2].trim());
					} else {
						avm.setAuthorName(artist);
					}
					Map<String, String> urlVariables = new HashMap<String, String>();
					urlVariables.put("author_name", avm.getAuthorName());
					urlVariables.put("nationality", avm.getNationality());
					urlVariables.put("limit", "(0,1)");
					RestTemplate rest = new RestTemplate();
					String uri = "http://localhost:8080/ace/paints/author/query?author_name=" + avm.getAuthorName()
							+ "&nationality=" + avm.getNationality() + "&limit=" + "(0,1)";
					ResponseEntity<ListViewModel> response = rest.getForEntity(uri, ListViewModel.class, urlVariables);
					ListViewModel<AuthorViewModel> lvm = response.getBody();
					if (CollectionUtils.isNotEmpty(lvm.getItems())) {
						authorId = ObjectUtils
								.fromJson(ObjectUtils.toJson(lvm.getItems().get(0)), AuthorViewModel.class)
								.getAuthorId();
					} else {
						try {
							// 调用创建名画作者接口
							RestTemplate restTemplate = new RestTemplate();
							avm = restTemplate.postForObject("http://localhost:8080/ace/paints/author", avm,
									AuthorViewModel.class);
							authorId = avm.getAuthorId();
						} catch (Exception e) {
							LOG.error("创建名画作者出错", e.getMessage());
							LOG.error("++++++++++++++++++++");
							System.out.println("@@@@@@@@@@");
							continue;
						}
					} // if-else
					rowMap.put("authorId", authorId);
					rowMap.put("ID", id);
					rowMap.put("artist", artist);
					rowMap.put("title", title);
					rowMap.put("continent", continent);
					rowMap.put("nation", nation);
					rowMap.put("objectType", objectType);
					rowMap.put("date", date);
					rowMap.put("medium", medium);
					rowMap.put("dimensions", dimensions);
					rowMap.put("currentLocation", currentLocation);
					list.add(rowMap);
				}
				writeToExcel(list);

			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.ImportExcelSuccess);
	}

	/**
	 * 导入名画信息文件
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/import/paints")
	public Map<String, String> importPaints(HttpServletRequest request) {
		CommonsMultipartResolver resolve = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (resolve.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multiRequest.getFile("paints");

			try {
				BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
				WorkbookSettings setting = new WorkbookSettings();
				setting.setEncoding("ISO-8859-1");
				Workbook workbook = Workbook.getWorkbook(bis, setting);
				Sheet sheet = workbook.getSheet(0);
				for (int i = 1; i < sheet.getRows(); i++) {
					String uuid = UUID.randomUUID().toString();
					String authorId = sheet.getCell(0, i).getContents();
					String id = sheet.getCell(1, i).getContents();
					String artist = sheet.getCell(2, i).getContents();
					String title = sheet.getCell(3, i).getContents();
					String continent = sheet.getCell(4, i).getContents();
					String objectType = sheet.getCell(6, i).getContents();
					String date = sheet.getCell(7, i).getContents();
					String medium = sheet.getCell(8, i).getContents();
					String dimensions = sheet.getCell(9, i).getContents();
					String currentLocation = sheet.getCell(10, i).getContents();
					// 读取名画文件，上传到服务器
					String path = "D:\\名画图片及信息表\\";
					List<String> paintList = getFile(path + id);
					for (String s : paintList) {
						File picture = new File(path + id + "\\" + s);
						dealFile(picture, "paints", uuid, s);
					}
					List<String> sortlist = new ArrayList<String>();
					while (paintList.size() > 0) {
						String minJpg = getMinValue(paintList);
						sortlist.add(minJpg);
						paintList.remove(minJpg);
					}
					String sourceJpg = sortlist.get(sortlist.size() - 1);
					String firstJpg = sortlist.get(0);
					String lastJpg = sortlist.get(sortlist.size() - 2);
					int middle = (sortlist.size() - 1) / 2;
					String middleJpg = sortlist.get(middle);

					PaintViewModel pvm = new PaintViewModel();
					if (dimensions != null && dimensions != "") {
						String[] content = dimensions.split("×|\\(|\\)|x");
						// 1. 只有h × w和 w×h(Without frame)
						if (content.length == 2 || content.length == 3) {
							if (content[0].trim().startsWith("w")) {
								pvm.setWidth(content[0].trim().substring(1));
							}
							if (content[1].trim().startsWith("h")) {
								pvm.setHeight(content[1].trim().substring(1));
							}
						}
						// 2. w×h(w×h)
						else if (content.length == 4) {
							if (dimensions.indexOf("d") == -1) {
								if (content[0].trim().startsWith("w")) {
									pvm.setWidth(content[0].substring(1) + "(" + content[2].trim() + ")");
								}
								if (content[1].trim().startsWith("h")) {
									pvm.setHeight(content[1].trim().substring(1) + "(" + content[3].trim() + ")");
								}
							}
							// 3.w×h×d()
							if (dimensions.indexOf("d") != -1) {
								pvm.setWidth(content[0] + "×" + content[1].trim());
								pvm.setHeight(content[2].trim());
							}
						} else {
						}
					}
					pvm.setAuthorId(authorId);
					pvm.setTitle(title);
					pvm.setObjectType(objectType);
					pvm.setWriteDate(date);
					pvm.setMedium(medium);
					pvm.setCurrentLocation(currentLocation);
					pvm.setEstatus("ONLINE");
					List<String> tags = new ArrayList<String>();
					tags.add(title);
					tags.add(artist);
					tags.add(objectType);
					tags.add(medium);
					tags.add(currentLocation);
					tags.add(continent);
					pvm.setTags(tags);

					// 设置tech_info
					ResTechInfoViewModel rtvm = new ResTechInfoViewModel();
					rtvm.setLocation("${ref-path}/" + "/esp/paints/" + uuid + ".pkg/" + sourceJpg);
					rtvm.setFormat("image/jpg");
					rtvm.setMd5("md5");
					rtvm.setEntry("入口地址");
					ResTechInfoViewModel firstRtvm = new ResTechInfoViewModel();
					firstRtvm.setLocation("${ref-path}/" + "/esp/paints/" + uuid + ".pkg/" + firstJpg);
					firstRtvm.setFormat("image/jpg");
					firstRtvm.setMd5("md5");
					firstRtvm.setEntry("入口地址");
					ResTechInfoViewModel middleRtvm = new ResTechInfoViewModel();
					middleRtvm.setLocation("${ref-path}/" + "/esp/paints/" + uuid + ".pkg/" + middleJpg);
					middleRtvm.setFormat("image/jpg");
					middleRtvm.setMd5("md5");
					middleRtvm.setEntry("入口地址");
					ResTechInfoViewModel lastRtvm = new ResTechInfoViewModel();
					lastRtvm.setLocation("${ref-path}/" + "/esp/paints/" + uuid + ".pkg/" + lastJpg);
					lastRtvm.setFormat("image/jpg");
					lastRtvm.setMd5("md5");
					lastRtvm.setEntry("入口地址");
					List<TechnologyRequirementViewModel> requirementsList = new ArrayList<TechnologyRequirementViewModel>();
					rtvm.setRequirements(requirementsList);
					Map<String, ResTechInfoViewModel> techInfoMap = new HashMap<>();
					techInfoMap.put("300", firstRtvm);
					techInfoMap.put("800", middleRtvm);
					techInfoMap.put("1000", lastRtvm);
					techInfoMap.put("source", rtvm);
					pvm.setTechInfo(techInfoMap);

					try {
						// 调用创建名画接口
						RestTemplate rest = new RestTemplate();
						rest.postForObject("http://localhost:8080/ace/paints/create", pvm, PaintViewModel.class);
					} catch (Exception e) {
						LOG.error("创建名画出错", e.getMessage());
						LOG.error("++++++++++++++++++++");
						System.out.println("%%%%%%%%%%%");
						continue;
					}

				}
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.ImportExcelSuccess);
	}

	private void writeToExcel(List<Map<String, String>> list) {

		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File("C:\\Users\\yuzhi\\Desktop\\paints.xls"));
			WritableSheet sheet = workbook.createSheet("Sheet1", 0);
			String[] title = { "authorId", "ID", "artist", "title", "continent", "nation", "objectType", "date",
					"medium", "dimensions", "currentLocation" };
			// 标题
			for (int i = 0; i < title.length; i++) {
				sheet.addCell(new Label(i, 0, title[i]));
			}
			// 填充数据
			Map<String, String> tempMap = null;
			for (int row = 0; row < list.size(); row++) {
				int col = 0;
				// 第0行为标题，故写入excel从第一行开始
				tempMap = list.get(row);
				sheet.addCell(new Label(col++, row + 1, tempMap.get("authorId")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("ID")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("artist")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("title")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("continent")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("nation")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("objectType")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("date")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("medium")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("dimensions")));
				sheet.addCell(new Label(col++, row + 1, tempMap.get("currentLocation")));
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			LOG.error("导出名画信息异常：" + e);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.ExportExcelFail.getCode(),
					e.getMessage());
		}
	}

	private Map<String, Object> dealFile(File f, String resType, String id, String newFileName) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpclient = httpClientBuilder.build();
		try {
			String newDirPath = "/esp/" + resType + "/" + id + ".pkg";

			String url = "http://localhost:8080/ace/file/yzc/upload";

			HttpPost httpPost = new HttpPost(url);
			MultipartEntityBuilder partBuilder = MultipartEntityBuilder.create();
			// 设置浏览器兼容模式，解决文件名乱码问题
			partBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			InputStreamBody inBody = new InputStreamBody(new FileInputStream(f), ContentType.create("application/zip"),
					newFileName);
			partBuilder.addPart("path", new StringBody(newDirPath, ContentType.APPLICATION_JSON));
			partBuilder.addPart("name", new StringBody(newFileName, ContentType.APPLICATION_JSON));
			partBuilder.addPart("file", inBody);
			partBuilder.setCharset(Charset.forName("utf-8"));
			HttpEntity reqEntity = partBuilder.build();
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine == null || statusLine.getStatusCode() != 200) {
				LOG.error("文件上传异常！newDirPath:" + newDirPath + "=======newFileName:" + newFileName);
				LOG.error(response.toString());
			} else {
				InputStream in = response.getEntity().getContent();
				try {
					String rt = IOUtils.toString(in, "utf-8");
					@SuppressWarnings("unchecked")
					Map<String, Object> map = ObjectUtils.fromJson(rt, Map.class);
					return map;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (in != null)
						in.close();
				}
				LOG.info("文件处理成功！newDirpath:" + newDirPath + "=========newFileName:" + newFileName);
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@");
			}
		} catch (Exception e) {
			LOG.error("文件处理异常！resType:{1},id:{2}", resType, id);
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getMinValue(List<String> paintList) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < paintList.size(); i++) {

			if (Character.isDigit(paintList.get(i).charAt(0))) {
				if (Character.isDigit(paintList.get(i).charAt(3))) {
					map.put(i, Integer.parseInt(paintList.get(i).substring(0, 4)));

				} else if (Character.isDigit(paintList.get(i).charAt(2))) {
					map.put(i, Integer.parseInt(paintList.get(i).substring(0, 3)));
				} else if (Character.isDigit(paintList.get(i).charAt(1))) {
					map.put(i, Integer.parseInt(paintList.get(i).substring(0, 2)));
				} else {
					map.put(i, Integer.parseInt(paintList.get(i).substring(0, 1)));
				}
			} else {
				map.put(i, 6553);
			}
		}
		int min = map.get(0);
		int n = 0;
		for (int j = 0; j < paintList.size(); j++) {

			if (min > map.get(j)) {
				min = map.get(j);
				n = j;
			}
		}
		return paintList.get(n);
	}

	private List<String> getFile(String path) {

		List<String> list = new ArrayList<String>();
		File file = new File(path);
		File[] array = file.listFiles();
		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				if (array[i].getName().endsWith("jpg") || array[i].getName().endsWith("jpeg")) {
					list.add(array[i].getName());
				}
			}
		}
		return list;
	}

	@RequestMapping(value = "/client/file/upload", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody Map<String, String> clientFileUpload() throws IOException {

		File picture = new File("D:\\Downloads\\资产信息确认数据2016.10.10.xlsx");
		dealFile(picture, "paints", "123456", "ace.xls");
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.UploadFileSuccess);
	}
}
