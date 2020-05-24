package wind.model.players.content;

import wind.model.players.Requirement;
import wind.model.players.Rights;
import wind.model.players.req.RankRequirement;


public enum Title {
	JUNIOR_CADET("<col=aa44aa>Junior Cadet</col>", new RankRequirement(Rights.ADMINISTRATOR)),
	;	
	@SuppressWarnings("unused")
	private final String title;
	
	@SuppressWarnings("unused")
	private final Requirement[] requirement;
	
	private Title(String title, Requirement... requirement) {
		this.title = title;
		this.requirement = requirement;
	}
}