/*
 * Universal Media Server, for streaming any medias to DLNA
 * compatible renderers based on the http://www.ps3mediaserver.org.
 * Copyright (C) 2012  UMS developers.
 *
 * This program is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 2
 * of the License only.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.pms.configuration;

import java.net.InetAddress;
import net.pms.Messages;
import net.pms.PMS;
import org.apache.commons.configuration.ConfigurationException;


public class WebRender extends RendererConfiguration {
	private String name;
	private String ip;
	private int browser = 0;
	private int port;
	private String ua;
	private static final PmsConfiguration configuration = PMS.getConfiguration();

	protected static final int CHROME = 1;
	protected static final int MSIE = 2;
	protected static final int FIREFOX = 3;
	protected static final int SAFARI = 4;
	protected static final int PS4 = 5;
	protected static final int XBOX1 = 6;
	protected static final int OPERA = 7;

	public WebRender(String name) throws ConfigurationException {
		super(null);
		this.name = name;
		ip = "";
		port = 0;
		ua = "";
	}

	public static String getBrowserName(int browser) {
		switch (browser) {
			case CHROME:  return "Chrome";
			case MSIE:    return "Internet Explorer";
			case FIREFOX: return "Firefox";
			case SAFARI:  return "Safari";
			case PS4:     return "Playstation 4";
			case XBOX1:   return "Xbox One";
			case OPERA:   return "Opera";
			default:      return Messages.getString("PMS.142");
		}
	}

	public static int getBrowser(String userAgent) {
		String ua = userAgent.toLowerCase();
		return
			ua.contains("chrome")        ? CHROME :
			(ua.contains("msie") ||
			ua.contains("trident"))      ? MSIE :
			ua.contains("firefox")       ? FIREFOX :
			ua.contains("safari")        ? SAFARI :
			ua.contains("playstation 4") ? PS4 :
			ua.contains("xbox one")      ? XBOX1 :
			ua.contains("opera")         ? OPERA :
			0;
	}

	public void setBrowserInfo(String info, String userAgent) {
		setUA(userAgent);
		browser = getBrowser(userAgent);
	}

	@Override
	public String getRendererName() {
		String rendererName = "";
		if (configuration.isWebAuthenticate()) {
			rendererName = name + "@";
		}

		if (ua.contains("chrome")) {
			rendererName += "Chrome";
		} else  if (ua.contains("msie")) {
			rendererName += "Internet Explorer";
		} else if (ua.contains("firefox")) {
			rendererName += "Firefox";
		} else if (ua.contains("safari")) {
			rendererName += "Safari";
		} else if (ua.contains("playstation 4")) {
			rendererName += "PlayStation 4";
		} else if (ua.contains("xbox one")) {
			rendererName += "Xbox One";
		} else {
			rendererName += Messages.getString("PMS.142");
		}

		return rendererName;
	}

	@Override
	public void associateIP(InetAddress sa) {
		super.associateIP(sa);
		ip = sa.getHostAddress();
	}

	public InetAddress getAddress() {
		try {
			return InetAddress.getByName(ip);
		} catch (Exception e) {
			return null;
		}
	}

	public void associatePort(int port) {
		this.port = port;
	}

	public void setUA(String ua) {
		this.ua = ua.toLowerCase();
	}

	@Override
	public String getRendererIcon() {
		switch (browser) {
			case CHROME:  return "chrome.png";
			case MSIE:    return "internetexplorer.png";
			case FIREFOX: return "firefox.png";
			case SAFARI:  return "safari.png";
			case PS4:     return "ps4.png";
			case XBOX1:   return "xbox-one.png";
			case OPERA:   return "opera.png";
			default:      return super.getRendererIcon();
		}
	}

	@Override
	public String toString() {
		return getRendererName();
	}

	@Override
	public boolean isMediaParserV2ThumbnailGeneration() {
		return false;
	}

	@Override
	public boolean isLimitFolders() {
		// no folder limit on the web clients
		return false;
	}

	public boolean isChromeTrick() {
		return browser == CHROME && configuration.getWebChrome();
	}

}
