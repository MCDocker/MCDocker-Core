/*
 *
 *   MCDocker, an open source Minecraft launcher.
 *   Copyright (C) 2021 MCDocker
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.mcdocker.launcher.utils;

import java.util.List;

public class Table {

    private List<String> headers;
    private List<List<String>> rows;

    public Table() {}
    public Table(List<String> headers, List<List<String>> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        int[] widths = new int[headers.size()];
        for (int i = 0; i < headers.size(); i++)
            widths[i] = headers.get(i).length();

        for (List<String> row : rows)
            for (int i = 0; i < row.size(); i++)
                if (row.get(i).length() > widths[i]) widths[i] = row.get(i).length();

        for (int i = 0; i < headers.size(); i++)
            sb.append(String.format("%-" + (widths[i]) + "s  ", headers.get(i).toUpperCase()));

        sb.append("\n");

        for (int k = 0; k < rows.size(); k++) {
            List<String> row = rows.get(k);
            for (int i = 0; i < row.size(); i++) sb.append(String.format(" %-" + widths[i] + "s ", row.get(i)));
            sb.append("\n");
        }

        return sb.toString();
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

    public void addRows(List<String> rows) {
        this.rows.add(rows);
    }

    public void addHeader(String header) {
        this.headers.add(header);
    }

}
