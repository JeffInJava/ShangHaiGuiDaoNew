package androidPT.ptUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.ParseException;

import android.text.TextUtils;

import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

public class SearchUtil {
	private static List<TreeVo> resultList;
	private static String name;
	private static String docNum;
	private static String ruleNum;
	private static String departmentNos;
	private static String pdfName;
	private static List<Map<String, Object>> indexResultList;

	/**
	 * 仅按部门查询
	 * 
	 * @param treeList
	 *            查询树
	 * @param depNo
	 *            部门编号
	 * @return 查询到的子节点（TreeVo）列表
	 */
	public static List<TreeVo> searchByDepWithoutKey(List<TreeVo> treeList,
			String depNo) {
		resultList = new ArrayList<TreeVo>();
		if (treeList != null) {
			for (TreeVo vo : treeList) {
				getVoValues(vo);

				boolean isLast = true;

				for (TreeVo vo2 : treeList) {
					if (vo2.getParentId().equals(vo.getId())) {
						isLast = false;
					}
				}
				if (isLast) {

					if (TextUtils.isEmpty(depNo)) {
						if (!TextUtils.isEmpty(pdfName)) {
							resultList.add(vo);
						}
					} else {
						if (departmentNos.contains(depNo)) {
							resultList.add(vo);
						}
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * 按文件名查询
	 * 
	 * @param treeList
	 *            查询树
	 * @param depNo
	 *            部门编号
	 * @param key
	 *            文件名
	 * @return 查询到的子节点（TreeVo）列表
	 */
	public static List<TreeVo> searchByDepWithName(List<TreeVo> treeList,
			String depNo, String key) {
		resultList = new ArrayList<TreeVo>();
		if (treeList != null) {
			for (TreeVo vo : treeList) {
				getVoValues(vo);
				if (name.contains(key)) {
					boolean isLast = true;

					for (TreeVo vo2 : treeList) {
						if (vo2.getParentId().equals(vo.getId())) {
							isLast = false;
						}
					}

					if (isLast) {
						if (depNo.isEmpty()) {
							resultList.add(vo);
						} else if (departmentNos.contains(depNo)) {
							resultList.add(vo);
						}
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * 按文号查询
	 * 
	 * @param treeList
	 *            查询树
	 * @param depNo
	 *            部门编号
	 * @param key
	 *            文号
	 * @return 查询到的子节点（TreeVo）列表
	 */
	public static List<TreeVo> searchByDepWithDocNum(List<TreeVo> treeList,
			String depNo, String key) {
		resultList = new ArrayList<TreeVo>();
		if (treeList != null) {
			for (TreeVo vo : treeList) {
				getVoValues(vo);

				boolean isLast = true;

				for (TreeVo vo2 : treeList) {
					if (vo2.getParentId().equals(vo.getId())) {
						isLast = false;
					}
				}
				if (isLast) {

					if (docNum.contains(key)) {
						if (depNo.isEmpty()) {
							resultList.add(vo);
						} else if (departmentNos.contains(depNo)) {
							resultList.add(vo);
						}
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * 按企标号查询
	 * 
	 * @param treeList
	 *            查询树
	 * @param depNo
	 *            部门编号
	 * @param key
	 *            企标号
	 * @return 查询到的子节点（TreeVo）列表
	 */
	public static List<TreeVo> searchByDepWithRuleNum(List<TreeVo> treeList,
			String depNo, String key) {
		resultList = new ArrayList<TreeVo>();
		if (treeList != null) {
			for (TreeVo vo : treeList) {
				getVoValues(vo);

				boolean isLast = true;

				for (TreeVo vo2 : treeList) {
					if (vo2.getParentId().equals(vo.getId())) {
						isLast = false;
					}
				}
				if (isLast) {

					if (ruleNum.contains(key)) {
						if (depNo.isEmpty()) {
							resultList.add(vo);
						} else if (departmentNos.contains(depNo)) {
							resultList.add(vo);
						}
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * 全文检索
	 * 
	 * @param treeList
	 *            查询树
	 * @param indexFolderPath
	 *            索引文件所在文件夹路径
	 * @param depNo
	 *            部门号
	 * @param key
	 *            查询值
	 * @return 查询到的子节点（TreeVo）列表
	 */
	public static Map<String, Object> searchByIndexWithDep(
			List<TreeVo> treeList, String indexFolderPath, String depNo,
			String key) {
		resultList = new ArrayList<TreeVo>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (treeList != null) {
			try {
				indexResultList = LuceneUtil.searchPageContentIndex(
						indexFolderPath, key, depNo, 10);
				if (indexResultList.size() > 0) {
					for (Map<String, Object> map : indexResultList) {
						String pdfName = String.valueOf(map.get("pdfName"));
						for (TreeVo vo : treeList) {
							getVoValues(vo);

							boolean isLast = true;

							for (TreeVo vo2 : treeList) {
								if (vo2.getParentId().equals(vo.getId())) {
									isLast = false;
								}
							}
							if (isLast) {

								if (pdfName.equals(SearchUtil.pdfName)) {
									resultList.add(vo);
								}
							}
						}
					}

					resultMap.put("resultList", indexResultList);
					resultMap.put("fileList", resultList);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return resultMap;
	}

	private static void getVoValues(TreeVo vo) {
		name = vo.getName() == null ? "" : vo.getName();
		pdfName = vo.getPdfName() == null ? "" : vo.getPdfName();
		docNum = vo.getDocNum() == null ? "" : vo.getDocNum();
		ruleNum = vo.getRuleNum() == null ? "" : vo.getRuleNum();
		departmentNos = vo.getDepartmentNos() == null ? "" : vo
				.getDepartmentNos();
	}
}
