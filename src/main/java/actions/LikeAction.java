package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import services.ReportService;

public class LikeAction extends ActionBase {
	private ReportService service;

	public void process() throws ServletException, IOException {
		service = new ReportService();
		invoke();
		service.close();
	}

	public void update() throws ServletException, IOException {
		ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));
		rv.setLikeCount(rv.getLikeCount() + 1);
		List<String> errors = service.update(rv);
		if (errors.size() > 0) {
			putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
			putRequestScope(AttributeConst.REPORT, rv); //入力された日報情報
			putRequestScope(AttributeConst.ERR, errors); //エラーのリスト
			forward(ForwardConst.FW_REP_EDIT);
		} else {
			putSessionScope(AttributeConst.FLUSH, MessageConst.I_LIKE.getMessage());
			redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
		}
	}
}
