package com.yzc.service.impl;

import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yzc.entity.LogInfo;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.repository.LogInfoRepository;
import com.yzc.service.LogService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.enums.RecordStatus;

@Service("LogService")
@Transactional
public class LogServiceImpl implements LogService{
	
	private static final Logger LOG = LoggerFactory.getLogger(LogServiceImpl.class);
	@Autowired
	private LogInfoRepository logInfoRepository;

	@Override
	public void createLog(LogInfo log) {
		log.setIdentifier(UUID.randomUUID().toString());
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		log.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		log.setState(RecordStatus.NORMAL);
		LogInfo logInfo=null;
		try {
			logInfo = logInfoRepository.add(log);
		} catch (EspStoreException e) {
			LOG.error("创建日志出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (logInfo == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail);
		}
		
	}

}
