package net.pms.dlna;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.pms.PMS;
import net.pms.util.FileUtil;

public class FileSearch implements SearchObj {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileSearch.class);
	private ArrayList<RealFile> folders;

	public FileSearch(List<RealFile> folders) {
		this.folders = new ArrayList<>(folders);
	}

	public void update(List<RealFile> folders) {
		this.folders = (ArrayList<RealFile>) folders;
	}

	@Override
	public void search(String searchString, DLNAResource searcher) {
		searchString = searchString.toLowerCase();
		for (RealFile res : folders) {
			String name = res.getName().toLowerCase();
			if (name.contains(searchString)) {
				// easy case file contians search string, add it and be gone
				searcher.addChild(res.clone());
				continue;
			}
			if (res.getFile().isDirectory()) {
				// tricky case, recursive in to folders
				File file = res.getFile();
				try {
					searchFiles(FileUtil.listFiles(file), searchString, searcher, 0);
				} catch (IOException e) {
					LOGGER.error("IO error while enumerating \"{}\" content: {}", file.getAbsolutePath(), e.getMessage());
					LOGGER.trace("", e);
				}
			}
		}
	}

	private void searchFiles(File[] files, String str, DLNAResource searcher, int cnt) {
		if (files == null) {
			return;
		}
		for (File file : files) {
			String name = file.getName().toLowerCase();
			if (name.contains(str)) {
				searcher.addChild(new RealFile(file));
				continue;
			}
			if (file.isDirectory()) {
				if (cnt >= PMS.getConfiguration().getSearchDepth()) {
					// this is here to avoid endless looping
					return;
				}
				try {
					searchFiles(FileUtil.listFiles(file), str, searcher, cnt + 1);
				} catch (IOException e) {
					LOGGER.error("IO error while enumerating \"{}\" content: {}", file.getAbsolutePath(), e.getMessage());
					LOGGER.trace("", e);
				}
			}
		}
	}
}
