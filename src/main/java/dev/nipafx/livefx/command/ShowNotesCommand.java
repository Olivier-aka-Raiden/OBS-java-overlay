package dev.nipafx.livefx.command;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("show-notes")
public record ShowNotesCommand() implements Command {
}
