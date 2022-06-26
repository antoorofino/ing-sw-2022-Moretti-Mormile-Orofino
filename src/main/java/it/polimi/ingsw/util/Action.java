package it.polimi.ingsw.util;


import it.polimi.ingsw.model.*;

import java.io.Serializable;

public class Action implements Serializable {
        private final ActionType actionType;
        private Piece principalPiece;
        private Piece optionalPiece;
        private int integer;

        public Action(ActionType actionType, Piece principalPiece){
            this.actionType = actionType;
            this.principalPiece = principalPiece;
        }

        public Action(ActionType actionType, Piece principalPiece,Piece optionalPiece){
            this.actionType = actionType;
            this.principalPiece = principalPiece;
            this.optionalPiece = optionalPiece;
        }

        public Action(ActionType actionType, Piece principalPiece, int integer){
            this.actionType = actionType;
            this.principalPiece = principalPiece;
            this.integer = integer;
        }

        public Action(ActionType actionType, int integer){
            this.actionType = actionType;
            this.integer = integer;
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

        public Piece getOptionalPiece(){
            return this.optionalPiece;
        }

        public int getInteger(){
            return this.integer;
        }
}
