package com.redmadrobot.inputmask.model;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo15441bv = {1, 0, 2}, mo15442d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0002\b\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0007HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00072\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\r¨\u0006\u0017"}, mo15443d2 = {"Lcom/redmadrobot/inputmask/model/Notation;", "", "character", "", "characterSet", "", "isOptional", "", "(CLjava/lang/String;Z)V", "getCharacter", "()C", "getCharacterSet", "()Ljava/lang/String;", "()Z", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "inputmask_release"}, mo15444k = 1, mo15445mv = {1, 1, 9})
/* compiled from: Notation.kt */
public final class Notation {
    private final char character;
    @NotNull
    private final String characterSet;
    private final boolean isOptional;

    @NotNull
    public static /* bridge */ /* synthetic */ Notation copy$default(Notation notation, char c, String str, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            c = notation.character;
        }
        if ((i & 2) != 0) {
            str = notation.characterSet;
        }
        if ((i & 4) != 0) {
            z = notation.isOptional;
        }
        return notation.copy(c, str, z);
    }

    public final char component1() {
        return this.character;
    }

    @NotNull
    public final String component2() {
        return this.characterSet;
    }

    public final boolean component3() {
        return this.isOptional;
    }

    @NotNull
    public final Notation copy(char c, @NotNull String str, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "characterSet");
        return new Notation(c, str, z);
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof Notation) {
                Notation notation = (Notation) obj;
                if ((this.character == notation.character) && Intrinsics.areEqual((Object) this.characterSet, (Object) notation.characterSet)) {
                    if (this.isOptional == notation.isOptional) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = this.character * 31;
        String str = this.characterSet;
        int hashCode = (i + (str != null ? str.hashCode() : 0)) * 31;
        boolean z = this.isOptional;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Notation(character=");
        sb.append(this.character);
        sb.append(", characterSet=");
        sb.append(this.characterSet);
        sb.append(", isOptional=");
        sb.append(this.isOptional);
        sb.append(")");
        return sb.toString();
    }

    public Notation(char c, @NotNull String str, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "characterSet");
        this.character = c;
        this.characterSet = str;
        this.isOptional = z;
    }

    public final char getCharacter() {
        return this.character;
    }

    @NotNull
    public final String getCharacterSet() {
        return this.characterSet;
    }

    public final boolean isOptional() {
        return this.isOptional;
    }
}
