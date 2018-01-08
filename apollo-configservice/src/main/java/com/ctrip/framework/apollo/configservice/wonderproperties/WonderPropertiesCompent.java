package com.ctrip.framework.apollo.configservice.wonderproperties;

import com.ctrip.framework.apollo.biz.entity.Namespace;
import com.ctrip.framework.apollo.biz.repository.ItemRepository;
import com.ctrip.framework.apollo.biz.service.AppService;
import com.ctrip.framework.apollo.biz.service.ItemSetService;
import com.ctrip.framework.apollo.biz.service.NamespaceService;
import com.ctrip.framework.apollo.biz.service.ReleaseService;
import com.ctrip.framework.apollo.common.dto.ItemChangeSets;
import com.ctrip.framework.apollo.common.dto.ItemDTO;
import com.ctrip.framework.apollo.common.utils.BeanUtils;
import com.ctrip.framework.apollo.core.NetposaConstant;
import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class WonderPropertiesCompent {

	private static final Logger logger = LoggerFactory.getLogger(WonderPropertiesCompent.class);

	@Autowired
	private BaseProperties baseProperties;
	@Autowired
	private ViasINI viasINI;
	@Autowired
	private ItemSetService itemSetService;
	@Autowired
	private NamespaceService namespaceService;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ReleaseService releaseService;

	private String userId = "admin";

	/**
	 * 系统初始化
	 */
	public void initSystem() throws Exception {
		this.initProperties();
	}

	private void initProperties() throws IOException {
		Namespace namespace = namespaceService.findOne(NetposaConstant.default_appId,
				NetposaConstant.default_clusterName, NetposaConstant.default_full_namespace);
		if (namespace != null && NetposaConstant.WONDER_PROPERTIES_FLAG) {
			ItemChangeSets propertiesItems = createItems(namespace.getId());
			logger.debug("propertiesItems is empty:{}", propertiesItems.isEmpty());
			//todo 已经没有变化的配置项了 全是新增下个版本去掉
			if (!propertiesItems.isEmpty()) {
				logger.debug("[配置新增:{}个]", propertiesItems.getCreateItems().size());
				logger.debug("[配置修改:{}个]", propertiesItems.getUpdateItems().size());
				logger.debug("[配置删除:{}个]", propertiesItems.getDeleteItems().size());

				//创建配置项
				itemSetService.updateSet(
						NetposaConstant.default_appId,
						NetposaConstant.default_clusterName,
						NetposaConstant.default_base_namespece,
						propertiesItems);

				String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");

				//发布
				releaseService.publish(namespace, time + " release",
						"系统初始化发布", userId, false);
			}

			logger.info("[配置中心默认配置项初始化完成]");
		}
	}

	//区分增加删除修改
	private ItemChangeSets createItems(Long defaultNamespaceId) throws IOException {
		List<ItemDTO> newList = this.parsePropertiesInfo(defaultNamespaceId);
		List<ItemDTO> oldList = BeanUtils.batchTransform(ItemDTO.class,
				itemRepository.findByNamespaceIdOrderByLineNumAsc(defaultNamespaceId));
		ItemChangeSets changeSet = new ItemChangeSets();
		for (ItemDTO newItem : newList) {
			boolean addflag = true;
			ListIterator<ItemDTO> listIterator = oldList.listIterator();
			while (listIterator.hasNext()) {
				ItemDTO oldItem = listIterator.next();
				if (newItem.getKey().equals(oldItem.getKey())) { //相同的key
					if (!newItem.getValue().equals(oldItem.getValue())) { //更新
						oldItem.setValue(newItem.getValue()); //更新值
						changeSet.addUpdateItem(oldItem);
					}
					addflag = false;
					listIterator.remove();
					break;
				}
			}
			if (addflag) { //新增
				changeSet.addCreateItem(newItem);
			}
		}
		changeSet.setDeleteItems(oldList); //删除
		changeSet.setDataChangeLastModifiedBy(userId);
		return changeSet;
	}

	private List<ItemDTO> parsePropertiesInfo(Long defaultNamespaceId) throws IOException {
		List<ItemDTO> list = new ArrayList<>();
		List<String> propertiesList = this.readLineByBasePros(baseProperties.getPropertiesFile());
		int i = 0;
		String comment = ""; //配置项的注释
		for (String line : propertiesList) {
			line = line.trim();
			if (StringUtils.isBlank(line)) {
				continue;
			}
			if (isComment(line)) {
				//是注释,为下一次配置信息的注释内容
				comment = line.substring(line.indexOf("#") + 1);
				list.add(new ItemDTO("", "", line , ++i , defaultNamespaceId)); //注释行
				continue;
			}
			if (isProperties(line)) {
				String[] item = line.split("=");
				String val = item[1].trim();
				String key = item[0].trim();
				int spiltStart = val.indexOf("$");
				if (spiltStart != -1) {
					int endStart = val.length();
					//结束位置
					if (val.contains(":")) {
						endStart = val.lastIndexOf(":"); //拼接
						String $temStr = val.substring(spiltStart, endStart);
						String temStr = val.substring(spiltStart + 1, endStart);
						val = val.replace($temStr, getReplaceValue(temStr));  //替换值
					} else if (val.contains("@")) {
						endStart = val.lastIndexOf("@");
						String port = val.substring(endStart + 1, val.length());
						val = val.substring(spiltStart + 1, endStart);
						val = getReplaceValue(val);
						val = StringUtils.join(Lists.transform(Arrays.asList(val.split(",")), new Function<String, String>() {
							@Override
							public String apply(String input) {
								return input + ":" + port;
							}
						}), ",");
					} else {
						val = val.substring(spiltStart + 1, endStart);
						val = getReplaceValue(val);
					}
				}
				list.add(new ItemDTO(key, val, comment, ++i, defaultNamespaceId));
			}
		}
		return list;
	}

	/**
	 * 是否为配置信息
	 */
	private boolean isProperties(String line) {
		return !line.startsWith("#") && line.contains("=");
	}

	/**
	 * 是否为注释行
	 */
	private boolean isComment(String line) {
		return line.startsWith("#");
	}


	/**
	 * 查找替换具体的值
	 */
	private String getReplaceValue(String template) {
		if (template.contains("$")) {
			template = template.substring(1);
		}
		ResourcePropertySource viasProperties = viasINI.getResourcePropertySource();
		String value = String.valueOf(viasProperties.getProperty(template));
		if (value.contains("$")) {
			int spiltStart = value.indexOf("$");
			int endStart = value.length();
			if (value.contains(",")) {
				List<String> mulitValue = new ArrayList<>();
				for (String key : value.split(",")) {
					mulitValue.add(getReplaceValue(key));
				}
				value = StringUtils.join(mulitValue, ",");
			} else {
				value = value.substring(spiltStart + 1, endStart);
				value = String.valueOf(viasProperties.getProperty(value));
				if (value.contains("$")) {
					//直到找到具体的值
					value = getReplaceValue(value);
				}
			}
		}
		if ("null".equals(value) || StringUtils.isBlank(value)) {
			logger.debug("配置项key->{}，没找到对应的配置项 value->{}", template, value);
		}
		return value;
	}


	/**
	 * 读取properties文件
	 */
	private List<String> readLineByBasePros(File propertiesFile) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(propertiesFile));
		final List<String> list = new ArrayList<String>();
		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}
		return list;
	}

}
