package org.jtester.wiki.assistor;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * 定位到resource文件指定行的描述信息
 * 
 * @author darui.wudr
 * 
 */
public class ResourceWithLineNo {
	private int line = 0;
	private IPath relPath;

	public ResourceWithLineNo(String path) {
		int lineIndex = path.indexOf(':');
		if (lineIndex > 0) {
			if (lineIndex < path.length() - 1) {
				this.line = Integer.parseInt(path.substring(lineIndex + 1)) - 1;
			}
			path = new String(path.substring(0, lineIndex));
		}
		this.relPath = new Path(path);
	}

	public int getLine() {
		return this.line;
	}

	public IPath getPath() {
		return this.relPath;
	}

	public int segmentCount() {
		return this.relPath.segmentCount();
	}
}