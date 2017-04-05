package net.pms.dlna.virtual;

import net.pms.Messages;
import net.pms.database.TableFilesStatus;
import net.pms.util.FullyPlayedAction;

/**
 * This is the Media Library folder which contains dynamic folders populated
 * by the SQL (h2) database.
 */
public class MediaLibrary extends VirtualFolder {
	private MediaLibraryFolder allFolder;

	public MediaLibraryFolder getAllFolder() {
		return allFolder;
	}
	private MediaLibraryFolder albumFolder;
	private MediaLibraryFolder artistFolder;
	private MediaLibraryFolder genreFolder;
	private MediaLibraryFolder playlistFolder;
	private MediaLibraryFolder tvShowsFolder;

	public MediaLibraryFolder getAlbumFolder() {
		return albumFolder;
	}

	public MediaLibrary() {
		super(Messages.getString("PMS.2"), null);
		init();
	}

	private void init() {
		// Videos folder
		VirtualFolder vfVideo = new VirtualFolder(Messages.getString("PMS.34"), null);

		// The following block contains all videos that are not fully played
		String sqlJoinStart = "SELECT * FROM FILES LEFT JOIN " + TableFilesStatus.TABLE_NAME + " ON FILES.ID = " + TableFilesStatus.TABLE_NAME + ".FILEID WHERE ";
		MediaLibraryFolder unwatchedTvShowsFolder = new MediaLibraryFolder(
			Messages.getString("VirtualFolder.4"),
			new String[]{
				"SELECT DISTINCT FILES.MOVIEORSHOWNAME FROM FILES LEFT JOIN " + TableFilesStatus.TABLE_NAME + " ON FILES.ID = " + TableFilesStatus.TABLE_NAME + ".FILEID WHERE FILES.TYPE = 4 AND FILES.ISTVEPISODE AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE                                                                                                                                  ORDER BY FILES.MOVIEORSHOWNAME ASC",
				"SELECT DISTINCT FILES.TVSEASON        FROM FILES LEFT JOIN " + TableFilesStatus.TABLE_NAME + " ON FILES.ID = " + TableFilesStatus.TABLE_NAME + ".FILEID WHERE FILES.TYPE = 4 AND FILES.ISTVEPISODE AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE AND FILES.MOVIEORSHOWNAME = '${0}'                                                                                               ORDER BY FILES.TVSEASON ASC",
				sqlJoinStart +                                                                                                                                                "FILES.TYPE = 4 AND FILES.ISTVEPISODE AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE AND FILES.MOVIEORSHOWNAME = '${1}' AND FILES.TVSEASON = '${0}' AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY FILES.TVEPISODENUMBER"
			},
			new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.SEASONS, MediaLibraryFolder.FILES_NOSORT}
		);
		MediaLibraryFolder unwatchedMoviesFolder = new MediaLibraryFolder(Messages.getString("VirtualFolder.5"), sqlJoinStart + "FILES.TYPE = 4 AND NOT ISTVEPISODE AND YEAR != '' AND STEREOSCOPY = '' AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder unwatchedMovies3DFolder = new MediaLibraryFolder(Messages.getString("VirtualFolder.7"), sqlJoinStart + "FILES.TYPE = 4 AND NOT ISTVEPISODE AND YEAR != '' AND STEREOSCOPY != '' AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder unwatchedUnsortedFolder = new MediaLibraryFolder(Messages.getString("VirtualFolder.8"), sqlJoinStart + "FILES.TYPE = 4 AND NOT ISTVEPISODE AND (YEAR IS NULL OR YEAR = '') AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder unwatchedAllVideosFolder = new MediaLibraryFolder(Messages.getString("PMS.35"), sqlJoinStart + "FILES.TYPE = 4 AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder unwatchedMlfVideo02 = new MediaLibraryFolder(
			Messages.getString("PMS.12"),
			new String[]{
				"SELECT FORMATDATETIME(MODIFIED, 'd MMM yyyy') FROM FILES LEFT JOIN " + TableFilesStatus.TABLE_NAME + " ON FILES.ID = " + TableFilesStatus.TABLE_NAME + ".FILEID WHERE FILES.TYPE = 4 AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY MODIFIED DESC",
				sqlJoinStart + "FILES.TYPE = 4 AND FORMATDATETIME(MODIFIED, 'd MMM yyyy') = '${0}' AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY FILENAME ASC"
			},
			new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES}
		);
		MediaLibraryFolder unwatchedMlfVideo03 = new MediaLibraryFolder(Messages.getString("PMS.36"), sqlJoinStart + "FILES.TYPE = 4 AND (WIDTH > 864 OR HEIGHT > 576) AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder unwatchedMlfVideo04 = new MediaLibraryFolder(Messages.getString("PMS.39"), sqlJoinStart + "FILES.TYPE = 4 AND (WIDTH <= 864 AND HEIGHT <= 576) AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder unwatchedMlfVideo05 = new MediaLibraryFolder(Messages.getString("PMS.40"), sqlJoinStart + "FILES.TYPE = 32 AND " + TableFilesStatus.TABLE_NAME + ".ISFULLYPLAYED IS NOT TRUE ORDER BY FILENAME ASC", MediaLibraryFolder.ISOS);

		// The following block contains all videos regardless of fully played status
		tvShowsFolder = new MediaLibraryFolder(
			Messages.getString("VirtualFolder.4"),
			new String[]{
				"SELECT DISTINCT MOVIEORSHOWNAME FROM FILES WHERE TYPE = 4 AND ISTVEPISODE ORDER BY MOVIEORSHOWNAME ASC",
				"SELECT DISTINCT TVSEASON        FROM FILES WHERE TYPE = 4 AND ISTVEPISODE AND MOVIEORSHOWNAME = '${0}' ORDER BY TVSEASON ASC",
				"SELECT          *               FROM FILES WHERE TYPE = 4 AND ISTVEPISODE AND MOVIEORSHOWNAME = '${1}' AND TVSEASON = '${0}' ORDER BY TVEPISODENUMBER"
			},
			new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.SEASONS, MediaLibraryFolder.FILES_NOSORT}
		);
		MediaLibraryFolder moviesFolder = new MediaLibraryFolder(Messages.getString("VirtualFolder.5"), "TYPE = 4 AND NOT ISTVEPISODE AND YEAR != '' AND STEREOSCOPY = '' ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder movies3DFolder = new MediaLibraryFolder(Messages.getString("VirtualFolder.7"), "TYPE = 4 AND NOT ISTVEPISODE AND YEAR != '' AND STEREOSCOPY != '' ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder unsortedFolder = new MediaLibraryFolder(Messages.getString("VirtualFolder.8"), "TYPE = 4 AND NOT ISTVEPISODE AND (YEAR IS NULL OR YEAR = '') ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder allVideosFolder = new MediaLibraryFolder(Messages.getString("PMS.35"), "TYPE = 4 ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder mlfVideo02 = new MediaLibraryFolder(
			Messages.getString("PMS.12"),
			new String[]{
				"SELECT FORMATDATETIME(MODIFIED, 'd MMM yyyy') FROM FILES WHERE TYPE = 4 ORDER BY MODIFIED DESC",
				"TYPE = 4 AND FORMATDATETIME(MODIFIED, 'd MMM yyyy') = '${0}' ORDER BY FILENAME ASC"
			},
			new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES}
		);
		MediaLibraryFolder mlfVideo03 = new MediaLibraryFolder(Messages.getString("PMS.36"), "TYPE = 4 AND (WIDTH > 864 OR HEIGHT > 576) ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder mlfVideo04 = new MediaLibraryFolder(Messages.getString("PMS.39"), "TYPE = 4 AND (WIDTH <= 864 AND HEIGHT <= 576) ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		MediaLibraryFolder mlfVideo05 = new MediaLibraryFolder(Messages.getString("PMS.40"), "TYPE = 32 ORDER BY FILENAME ASC", MediaLibraryFolder.ISOS);

		// If fully played videos are to be hidden
		if (configuration.getFullyPlayedAction() == FullyPlayedAction.HIDE_VIDEO) {
			vfVideo.addChild(unwatchedTvShowsFolder);
			vfVideo.addChild(unwatchedMoviesFolder);
			vfVideo.addChild(unwatchedMovies3DFolder);
			vfVideo.addChild(unwatchedUnsortedFolder);
			vfVideo.addChild(unwatchedAllVideosFolder);
			vfVideo.addChild(unwatchedMlfVideo02);
			vfVideo.addChild(unwatchedMlfVideo03);
			vfVideo.addChild(unwatchedMlfVideo04);
			vfVideo.addChild(unwatchedMlfVideo05);
		// If fully played videos are NOT to be hidden
		} else {
			VirtualFolder videosUnwatchedFolder = new VirtualFolder(Messages.getString("VirtualFolder.9"), null);
			videosUnwatchedFolder.addChild(unwatchedTvShowsFolder);
			videosUnwatchedFolder.addChild(unwatchedMoviesFolder);
			videosUnwatchedFolder.addChild(unwatchedMovies3DFolder);
			videosUnwatchedFolder.addChild(unwatchedUnsortedFolder);
			videosUnwatchedFolder.addChild(unwatchedAllVideosFolder);
			videosUnwatchedFolder.addChild(unwatchedMlfVideo02);
			videosUnwatchedFolder.addChild(unwatchedMlfVideo03);
			videosUnwatchedFolder.addChild(unwatchedMlfVideo04);
			videosUnwatchedFolder.addChild(unwatchedMlfVideo05);
			vfVideo.addChild(videosUnwatchedFolder);
			vfVideo.addChild(tvShowsFolder);
			vfVideo.addChild(moviesFolder);
			vfVideo.addChild(movies3DFolder);
			vfVideo.addChild(unsortedFolder);
			vfVideo.addChild(allVideosFolder);
			vfVideo.addChild(mlfVideo02);
			vfVideo.addChild(mlfVideo03);
			vfVideo.addChild(mlfVideo04);
			vfVideo.addChild(mlfVideo05);
		}
		addChild(vfVideo);

		VirtualFolder vfAudio = new VirtualFolder(Messages.getString("PMS.1"), null);
		allFolder = new MediaLibraryFolder(Messages.getString("PMS.11"), "select FILENAME, MODIFIED from FILES F, AUDIOTRACKS A where F.ID = A.FILEID AND F.TYPE = 1 ORDER BY F.FILENAME ASC", MediaLibraryFolder.FILES);
		vfAudio.addChild(allFolder);
		playlistFolder = new MediaLibraryFolder(Messages.getString("PMS.9"), "select FILENAME, MODIFIED from FILES F WHERE F.TYPE = 16 ORDER BY F.FILENAME ASC", MediaLibraryFolder.PLAYLISTS);
		vfAudio.addChild(playlistFolder);
		artistFolder = new MediaLibraryFolder(Messages.getString("PMS.13"), new String[]{"SELECT DISTINCT A.ARTIST FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 ORDER BY A.ARTIST ASC", "select FILENAME, MODIFIED  from FILES F, AUDIOTRACKS A where F.ID = A.FILEID AND F.TYPE = 1 AND A.ARTIST = '${0}'"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfAudio.addChild(artistFolder);
		albumFolder = new MediaLibraryFolder(Messages.getString("PMS.16"), new String[]{"SELECT DISTINCT A.ALBUM FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 ORDER BY A.ALBUM ASC", "select FILENAME, MODIFIED from FILES F, AUDIOTRACKS A where F.ID = A.FILEID AND F.TYPE = 1 AND A.ALBUM = '${0}'"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfAudio.addChild(albumFolder);
		genreFolder = new MediaLibraryFolder(Messages.getString("PMS.19"), new String[]{"SELECT DISTINCT A.GENRE FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 ORDER BY A.GENRE ASC", "select FILENAME, MODIFIED from FILES F, AUDIOTRACKS A where F.ID = A.FILEID AND F.TYPE = 1 AND A.GENRE = '${0}'"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfAudio.addChild(genreFolder);
		MediaLibraryFolder mlf6 = new MediaLibraryFolder(Messages.getString("PMS.22"), new String[]{
				"SELECT DISTINCT A.ARTIST FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 ORDER BY A.ARTIST ASC",
				"SELECT DISTINCT A.ALBUM FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 AND A.ARTIST = '${0}' ORDER BY A.ALBUM ASC",
				"select FILENAME, MODIFIED from FILES F, AUDIOTRACKS A where F.ID = A.FILEID AND F.TYPE = 1 AND A.ARTIST = '${1}' AND A.ALBUM = '${0}' ORDER BY A.TRACK ASC, F.FILENAME ASC"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfAudio.addChild(mlf6);
		MediaLibraryFolder mlf7 = new MediaLibraryFolder(Messages.getString("PMS.26"), new String[]{
				"SELECT DISTINCT A.GENRE FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 ORDER BY A.GENRE ASC",
				"SELECT DISTINCT A.ARTIST FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 AND A.GENRE = '${0}' ORDER BY A.ARTIST ASC",
				"SELECT DISTINCT A.ALBUM FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 AND A.GENRE = '${1}' AND A.ARTIST = '${0}' ORDER BY A.ALBUM ASC",
				"select FILENAME, MODIFIED from FILES F, AUDIOTRACKS A where F.ID = A.FILEID AND F.TYPE = 1 AND A.GENRE = '${2}' AND A.ARTIST = '${1}' AND A.ALBUM = '${0}' ORDER BY A.TRACK ASC, F.FILENAME ASC"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.TEXTS, MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfAudio.addChild(mlf7);
		MediaLibraryFolder mlfAudioDate = new MediaLibraryFolder(Messages.getString("PMS.12"), new String[]{"SELECT FORMATDATETIME(MODIFIED, 'd MMM yyyy') FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 ORDER BY F.MODIFIED DESC", "select FILENAME, MODIFIED from FILES F, AUDIOTRACKS A where F.ID = A.FILEID AND F.TYPE = 1 AND FORMATDATETIME(MODIFIED, 'd MMM yyyy') = '${0}' ORDER BY A.TRACK ASC, F.FILENAME ASC"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfAudio.addChild(mlfAudioDate);

		MediaLibraryFolder mlf8 = new MediaLibraryFolder(Messages.getString("PMS.28"), new String[]{
				"SELECT ID FROM REGEXP_RULES ORDER BY ORDR ASC",
				"SELECT DISTINCT A.ARTIST FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 AND A.ARTIST REGEXP (SELECT RULE FROM REGEXP_RULES WHERE ID = '${0}') ORDER BY A.ARTIST ASC",
				"SELECT DISTINCT A.ALBUM FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 AND A.ARTIST = '${0}' ORDER BY A.ALBUM ASC",
				"SELECT FILENAME, MODIFIED FROM FILES F, AUDIOTRACKS A WHERE F.ID = A.FILEID AND F.TYPE = 1 AND A.ARTIST = '${1}' AND A.ALBUM = '${0}'"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.TEXTS, MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfAudio.addChild(mlf8);
		addChild(vfAudio);

		VirtualFolder vfImage = new VirtualFolder(Messages.getString("PMS.31"), null);
		MediaLibraryFolder mlfPhoto01 = new MediaLibraryFolder(Messages.getString("PMS.32"), "TYPE = 2 ORDER BY FILENAME ASC", MediaLibraryFolder.FILES);
		vfImage.addChild(mlfPhoto01);
		MediaLibraryFolder mlfPhoto02 = new MediaLibraryFolder(Messages.getString("PMS.12"), new String[]{"SELECT FORMATDATETIME(MODIFIED, 'd MMM yyyy') FROM FILES WHERE TYPE = 2 ORDER BY MODIFIED DESC", "TYPE = 2 AND FORMATDATETIME(MODIFIED, 'd MMM yyyy') = '${0}' ORDER BY FILENAME ASC"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfImage.addChild(mlfPhoto02);
		MediaLibraryFolder mlfPhoto03 = new MediaLibraryFolder(Messages.getString("PMS.21"), new String[]{"SELECT MODEL FROM FILES WHERE TYPE = 2 AND MODEL IS NOT NULL ORDER BY MODEL ASC", "TYPE = 2 AND MODEL = '${0}' ORDER BY FILENAME ASC"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfImage.addChild(mlfPhoto03);
		MediaLibraryFolder mlfPhoto04 = new MediaLibraryFolder(Messages.getString("PMS.25"), new String[]{"SELECT ISO FROM FILES WHERE TYPE = 2 AND ISO > 0 ORDER BY ISO ASC", "TYPE = 2 AND ISO = '${0}' ORDER BY FILENAME ASC"}, new int[]{MediaLibraryFolder.TEXTS, MediaLibraryFolder.FILES});
		vfImage.addChild(mlfPhoto04);
		addChild(vfImage);
	}

	public MediaLibraryFolder getArtistFolder() {
		return artistFolder;
	}

	public MediaLibraryFolder getGenreFolder() {
		return genreFolder;
	}

	public MediaLibraryFolder getPlaylistFolder() {
		return playlistFolder;
	}
}
