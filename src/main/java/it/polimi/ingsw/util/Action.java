package it.polimi.ingsw.util;


import it.polimi.ingsw.model.*;

import java.io.Serializable;

public class Action implements Serializable {
        private ActionType actionType;
        private Piece principalPiece;
        private Piece optionalPiece;
        private int ID;

        public Action(ActionType actionType, Piece principalPiece, Piece optionalPiece, int ID) {
            this.actionType = actionType;
            this.principalPiece= principalPiece;
            this.optionalPiece= optionalPiece;
            this.ID= ID;
        }

        public Action(ActionType actionType) {
            this.actionType = actionType;
        }

        public ActionType getActionType(){
            return this.actionType;
        }

        public Piece getPrincipalPiece(){
            return this.principalPiece;
        }

        public void setPrincipalPiece(Piece piece) {
            this.principalPiece = piece;
        }

        public void setOptionalPiece(Piece piece){
            this.optionalPiece = piece;
        }

        public Piece getOptionalPiece(){
            return this.optionalPiece;
        }

        public void setID(int ID){
            this.ID = ID;
        }

        public int getID(){
            return this.ID;
        }
}
